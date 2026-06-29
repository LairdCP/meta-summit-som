# FIT image signing with AWS KMS — for recipes using kernel-fitimage or custom-fit-gen.
# Activated by inheriting conditionally: inherit ${@'fitimage-aws-sign' if any(d.getVar(v) for v in ('AWS_KMS_KEY_ARN','AWS_KMS_FIT_KEY_ARN')) else ''}

inherit uboot-aws-sign-common

# Use the FIT-specific ARN if set, otherwise fall back to the umbrella ARN.
# The class is only inherited when at least one of these is set, so by the time
# we get here, the effective ARN is always non-empty.
AWS_KMS_FIT_KEY_ARN ?= "${AWS_KMS_KEY_ARN}"

# Stage FIT signing wrappers in WORKDIR so we never overwrite any existing
# keys in the original UBOOT_SIGN_KEYDIR.
KMS_FIT_KEYDIR = "${WORKDIR}/kms-fit-keys"
KMS_FIT_KEYDIR_ORIG := "${UBOOT_SIGN_KEYDIR}"
UBOOT_SIGN_KEYDIR = "${KMS_FIT_KEYDIR}"

_fitimage_aws_kms_setup() {
    uboot_aws_kms_setup_env

    # Place PEM wrapper and certificate in the staged key directory
    if [ -n "${UBOOT_SIGN_KEYDIR}" ]; then
        mkdir -p "${UBOOT_SIGN_KEYDIR}"
        FIT_TOKEN="${@aws_kms_token_label(d.getVar('AWS_KMS_FIT_KEY_ARN'))}"
        if ! uboot_aws_kms_gen_wrapper "$FIT_TOKEN" \
            "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key"; then
            bbfatal "AWS KMS key validation failed for FIT key (AWS_KMS_FIT_KEY_ARN)." \
                    "Verify that AWS credentials are configured, the ARN is correct," \
                    "and the IAM role has kms:GetPublicKey permission."
        fi
        uboot_aws_kms_gen_cert \
            "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key" \
            "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.crt" \
            "${UBOOT_SIGN_KEYNAME}"

        if [ -n "${UBOOT_SIGN_IMG_KEYNAME}" ] && \
           [ "${UBOOT_SIGN_IMG_KEYNAME}" != "${UBOOT_SIGN_KEYNAME}" ]; then
            if ! uboot_aws_kms_gen_wrapper "$FIT_TOKEN" \
                "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_IMG_KEYNAME}.key"; then
                bbfatal "AWS KMS key validation failed for FIT IMG key (AWS_KMS_FIT_KEY_ARN)." \
                        "Verify that AWS credentials are configured, the ARN is correct," \
                        "and the IAM role has kms:GetPublicKey permission."
            fi
            uboot_aws_kms_gen_cert \
                "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_IMG_KEYNAME}.key" \
                "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_IMG_KEYNAME}.crt" \
                "${UBOOT_SIGN_IMG_KEYNAME}"
        fi
    fi
}

# For kernel-fitimage: replace local key generation with PEM wrapper placement
do_kernel_generate_rsa_keys:prepend() {
    _fitimage_aws_kms_setup
}
# Skip local key generation — keys live in KMS, certs are pre-deployed.
FIT_GENERATE_KEYS = "0"

do_assemble_fitimage:prepend() {
    _fitimage_aws_kms_setup
}

do_assemble_fitimage_initramfs:prepend() {
    _fitimage_aws_kms_setup
}

# For custom-fit-gen users (e.g. summit-mcu-demos) where signing happens in do_compile
do_compile:prepend() {
    _fitimage_aws_kms_setup
}
