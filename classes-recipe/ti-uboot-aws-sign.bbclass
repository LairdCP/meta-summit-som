# TI FIT-only image signing via AWS KMS.
#
# Generates a PKCS#11 key wrapper and self-signed certificate in a staging
# directory so mkimage signs via KMS instead of a local key.

inherit uboot-aws-sign-common

# Redirect UBOOT_SIGN_KEYDIR to staging when AWS_KMS_FIT_KEY_ARN is set.
UBOOT_SIGN_KEYDIR = "${@d.getVar('KMS_SIG_STAGING') + '/keys' if d.getVar('AWS_KMS_FIT_KEY_ARN') else d.getVar('KMS_SIGN_KEYDIR_ORIG')}"

do_compile:prepend() {
    uboot_aws_kms_setup_env

    if [ -n "${UBOOT_SIGN_KEYNAME}" ]; then
        FIT_TOKEN="${@aws_kms_token_label(d.getVar('AWS_KMS_FIT_KEY_ARN'))}"
        if [ -n "$FIT_TOKEN" ]; then
            mkdir -p "${KMS_SIG_STAGING}/keys"
            if ! uboot_aws_kms_gen_wrapper "$FIT_TOKEN" \
                "${KMS_SIG_STAGING}/keys/${UBOOT_SIGN_KEYNAME}.key"; then
                bbfatal "AWS KMS key validation failed for FIT key (AWS_KMS_FIT_KEY_ARN)." \
                        "Verify that AWS credentials are configured, the ARN is correct," \
                        "and the IAM role has kms:GetPublicKey permission."
            fi
            uboot_aws_kms_gen_cert \
                "${KMS_SIG_STAGING}/keys/${UBOOT_SIGN_KEYNAME}.key" \
                "${KMS_SIG_STAGING}/keys/${UBOOT_SIGN_KEYNAME}.crt" \
                "${UBOOT_SIGN_KEYNAME}"
        fi
    fi
}
