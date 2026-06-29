# NXP HAB4 + FIT image signing via AWS KMS.
#
# Stages SIG_DATA_PATH to a workdir copy with PKCS#11 key wrappers for CST
# (CSF/IMG keys) and optionally for FIT signing (dev.key → KMS wrapper).

inherit uboot-aws-sign-common

# Role-specific ARNs default to the umbrella AWS_KMS_KEY_ARN.
AWS_KMS_CSF_KEY_ARN ?= "${AWS_KMS_KEY_ARN}"
AWS_KMS_IMG_KEY_ARN ?= "${AWS_KMS_KEY_ARN}"

# Redirect SIG_DATA_PATH to the staging copy; UBOOT_SIGN_KEYDIR points into it.
KMS_SIG_DATA_ORIG := "${SIG_DATA_PATH}"
SIG_DATA_PATH = "${@d.getVar('KMS_SIG_STAGING') if d.getVar('KMS_SIG_DATA_ORIG') else ''}"
UBOOT_SIGN_KEYDIR = "${@d.getVar('KMS_SIG_STAGING') + '/keys' if d.getVar('KMS_SIG_DATA_ORIG') else d.getVar('KMS_SIGN_KEYDIR_ORIG')}"

do_compile:prepend() {
    uboot_aws_kms_setup_env

    # Stage signing data: create ${KMS_SIG_STAGING} with the crts/ from
    # the original SIG_DATA_PATH and a keys/ directory containing only the
    # PKCS#11 PEM wrappers CST actually needs.  No local private keys required.
    KMS_SIG_STAGE="${KMS_SIG_STAGING}"
    if [ -d "${KMS_SIG_DATA_ORIG}/crts" ]; then
        rm -rf "$KMS_SIG_STAGE"
        mkdir -p "$KMS_SIG_STAGE/keys"
        cp -a "${KMS_SIG_DATA_ORIG}/crts" "$KMS_SIG_STAGE/crts"

        # CST locates private keys relative to certs by swapping
        # crts/ → keys/ and _crt.pem → _key.pem.  Generate only the two
        # wrappers it will actually look for (derived from CSF_KEY/IMG_KEY).
        CSF_KEY_BASE=$(basename "${CSF_KEY}" | sed 's/_crt\.pem$/_key.pem/')
        if ! uboot_aws_kms_gen_wrapper \
            "${@aws_kms_token_label(d.getVar('AWS_KMS_CSF_KEY_ARN'))}" \
            "$KMS_SIG_STAGE/keys/$CSF_KEY_BASE"; then
            bbfatal "AWS KMS key validation failed for CSF key (AWS_KMS_CSF_KEY_ARN)." \
                    "Verify that AWS credentials are configured, the ARN is correct," \
                    "and the IAM role has kms:GetPublicKey permission."
        fi

        IMG_KEY_BASE=$(basename "${IMG_KEY}" | sed 's/_crt\.pem$/_key.pem/')
        if ! uboot_aws_kms_gen_wrapper \
            "${@aws_kms_token_label(d.getVar('AWS_KMS_IMG_KEY_ARN'))}" \
            "$KMS_SIG_STAGE/keys/$IMG_KEY_BASE"; then
            bbfatal "AWS KMS key validation failed for IMG key (AWS_KMS_IMG_KEY_ARN)." \
                    "Verify that AWS credentials are configured, the ARN is correct," \
                    "and the IAM role has kms:GetPublicKey permission."
        fi

        # CST reads the passphrase from keys/key_pass.txt — for PKCS#11
        # wrappers the passphrase is unused, but the file must exist.
        printf '\n\n' > "$KMS_SIG_STAGE/keys/key_pass.txt"

        # FIT signing key — only generate wrapper if AWS_KMS_FIT_KEY_ARN is set;
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
