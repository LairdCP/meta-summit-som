# NXP AHAB + FIT image signing via AWS KMS.
#
# Calls nxpimage directly with a PKCS#11 signature provider config string.
# SPSDK discovers the spsdk-pkcs11 plugin via entry points and delegates
# signing through python-pkcs11 -> aws_kms_pkcs11.so -> KMS.
#
# For FIT signing, the standard PKCS#11 PEM wrapper mechanism (via OpenSSL
# pkcs11-provider) is used for mkimage.

inherit uboot-aws-sign-common

# Role-specific ARN for the AHAB container signer key (ECC secp256r1).
AWS_KMS_AHAB_KEY_ARN ?= "${AWS_KMS_KEY_ARN}"

# Additional native dependency for the SPSDK PKCS#11 signature provider plugin.
DEPENDS += "python3-spsdk-pkcs11-native"

# Redirect UBOOT_SIGN_KEYDIR to staging copy for FIT key wrappers.
KMS_SIG_DATA_ORIG := "${SIG_DATA_PATH}"
UBOOT_SIGN_KEYDIR = "${@d.getVar('KMS_SIG_STAGING') + '/keys' if d.getVar('KMS_SIG_DATA_ORIG') else d.getVar('KMS_SIGN_KEYDIR_ORIG')}"

# AHAB signing runs during do_deploy -- needs network for KMS access.
do_deploy[network] = "1"

do_compile:prepend() {
    uboot_aws_kms_setup_env

    # Stage FIT signing key (dev.key -> PKCS#11 wrapper) if configured.
    KMS_SIG_STAGE="${KMS_SIG_STAGING}"
    if [ -d "${KMS_SIG_DATA_ORIG}" ]; then
        rm -rf "$KMS_SIG_STAGE"
        mkdir -p "$KMS_SIG_STAGE/keys"

        # FIT signing key -- only generate wrapper if AWS_KMS_FIT_KEY_ARN is set;
        # otherwise copy the original dev.key/dev.crt from SIG_DATA_PATH.
        if [ -n "${UBOOT_SIGN_KEYNAME}" ]; then
            FIT_TOKEN="${@aws_kms_token_label(d.getVar('AWS_KMS_FIT_KEY_ARN'))}"
            if [ -n "$FIT_TOKEN" ]; then
                if ! uboot_aws_kms_gen_wrapper "$FIT_TOKEN" \
                    "$KMS_SIG_STAGE/keys/${UBOOT_SIGN_KEYNAME}.key"; then
                    bbfatal "AWS KMS key validation failed for FIT key (AWS_KMS_FIT_KEY_ARN)." \
                            "Verify that AWS credentials are configured, the ARN is correct," \
                            "and the IAM role has kms:GetPublicKey permission."
                fi
                uboot_aws_kms_gen_cert \
                    "$KMS_SIG_STAGE/keys/${UBOOT_SIGN_KEYNAME}.key" \
                    "$KMS_SIG_STAGE/keys/${UBOOT_SIGN_KEYNAME}.crt" \
                    "${UBOOT_SIGN_KEYNAME}"
            elif [ -f "${KMS_SIG_DATA_ORIG}/keys/${UBOOT_SIGN_KEYNAME}.key" ]; then
                cp -a "${KMS_SIG_DATA_ORIG}/keys/${UBOOT_SIGN_KEYNAME}.key" \
                      "$KMS_SIG_STAGE/keys/${UBOOT_SIGN_KEYNAME}.key"
                if [ -f "${KMS_SIG_DATA_ORIG}/keys/${UBOOT_SIGN_KEYNAME}.crt" ]; then
                    cp -a "${KMS_SIG_DATA_ORIG}/keys/${UBOOT_SIGN_KEYNAME}.crt" \
                          "$KMS_SIG_STAGE/keys/${UBOOT_SIGN_KEYNAME}.crt"
                fi
            fi
        fi
    fi
}

# Override do_sign_boot_image with a PKCS#11 signature provider config string
# for AWS KMS remote signing.
do_sign_boot_image() {
    if [ ! -e "${KMS_SIG_DATA_ORIG}/spsdk_ahab.yaml" ]; then
        bbfatal "SPSDK config not found at '${KMS_SIG_DATA_ORIG}/spsdk_ahab.yaml'. " \
                "Ensure SIG_DATA_PATH is set and points at a PKI tree containing spsdk_ahab.yaml."
    fi

    if [ ! -e "${B}/flash.bin" ]; then
        bbfatal "imx-boot flash.bin is not available to sign"
    fi

    # Set up environment for aws_kms_pkcs11.so
    uboot_aws_kms_setup_env

    # Determine the PKCS#11 token and key labels from the AHAB key ARN.
    # Token label is truncated to 32 chars (CK_TOKEN_INFO.label limit).
    # Key label is the full UUID (CKA_LABEL is not truncated by aws_kms_pkcs11).
    AHAB_TOKEN="${@aws_kms_token_label(d.getVar('AWS_KMS_AHAB_KEY_ARN'))}"
    AHAB_KEY_LABEL="${@aws_kms_key_label(d.getVar('AWS_KMS_AHAB_KEY_ARN'))}"
    if [ -z "$AHAB_TOKEN" ]; then
        bbfatal "AWS_KMS_AHAB_KEY_ARN is not set or has no key ID"
    fi

    # Build the signer config string for SPSDK's SignatureProvider system.
    # The spsdk-pkcs11 plugin (type=pkcs11) talks directly to aws_kms_pkcs11.so
    # via python-pkcs11, performing ECDSA signing through AWS KMS.
    PKCS11_SO="${STAGING_LIBDIR_NATIVE}/pkcs11/aws_kms_pkcs11.so"
    SIGNER_CFG="type=pkcs11;so_path=${PKCS11_SO};token_label=${AHAB_TOKEN};key_label=${AHAB_KEY_LABEL};user_pin=unused"

    # Prepare the signing YAML: set family, rewrite signer line to use the
    # PKCS#11 signature provider, and absolutize certificate paths.
    AHAB_SIGN_YAML="${B}/spsdk_ahab_kms.yaml"
    sed -e "s|^ *family:.*|family: ${SPSDK_FAMILY}|" \
        -e "s|^ *signer:.*|signer: ${SIGNER_CFG}|" \
        -e "/srk_array/,/^[^ #]/{s|- \([^/][^ ]*\.pem\)|- ${KMS_SIG_DATA_ORIG}/crts/\1|}" \
        "${KMS_SIG_DATA_ORIG}/spsdk_ahab.yaml" > "${AHAB_SIGN_YAML}"

    bbnote "AHAB signing flash.bin for ${SPSDK_FAMILY} via SPSDK PKCS#11 provider"

    # Sign all OEM AHAB containers in flash.bin
    CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1 \
    nxpimage ahab sign \
        -c "${AHAB_SIGN_YAML}" \
        -b "${B}/flash.bin" \
        -o "${B}/signed-flash.bin" \
        --force

    if [ ! -e "${B}/signed-flash.bin" ]; then
        bbfatal "AHAB signing failed -- signed-flash.bin was not produced"
    fi

    rm -f "${AHAB_SIGN_YAML}"

    _deploy_signed_flash_bin
}
