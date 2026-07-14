# SPSDK family name mapping for AHAB devices
SPSDK_FAMILY:mx8ulp-generic-bsp = "mimx8ulp"
SPSDK_FAMILY:mx8qm-generic-bsp = "mimx8qm"
SPSDK_FAMILY:mx91-generic-bsp = "mimx9131"
SPSDK_FAMILY:mx93-generic-bsp = "mimx9352"
SPSDK_FAMILY:mx95-generic-bsp = "mimx9596"

SRC_URI:append:mx95-generic-bsp = " \
    file://0001-imx8image-Set-signature-block-version-1-for-AHAB-V2.patch \
    "

DEPENDS:append:mx8m-generic-bsp = " \
    cst-native \
    "

# binman's nxp-imx8mcst node (active when CONFIG_IMX_HAB is set) reads these
# from the environment to locate the HAB SRK table and CSF/IMG certificates for
# cst. They must be exported so every binman-invoking task (do_compile and
# do_uboot_assemble_fitimage) sees them. cst finds the matching private keys and
# key_pass.txt relative to these certs (sibling crts/ and keys/ directories).
SRK_TABLE:mx8m-generic-bsp ?= "${SIG_DATA_PATH}/crts/SRK_1_2_3_4_table.bin"
CSF_KEY:mx8m-generic-bsp ?= "${SIG_DATA_PATH}/crts/CSF1_1_sha256_2048_65537_v3_usr_crt.pem"
IMG_KEY:mx8m-generic-bsp ?= "${SIG_DATA_PATH}/crts/IMG1_1_sha256_2048_65537_v3_usr_crt.pem"

do_compile:prepend:mx8m-generic-bsp() {
    export SRK_TABLE="${SRK_TABLE}"
    export CSF_KEY="${CSF_KEY}"
    export IMG_KEY="${IMG_KEY}"
}

do_uboot_assemble_fitimage:prepend:mx8m-generic-bsp() {
    export SRK_TABLE="${SRK_TABLE}"
    export CSF_KEY="${CSF_KEY}"
    export IMG_KEY="${IMG_KEY}"
}

DEPENDS:append:mx9-generic-bsp = " \
    python3-spsdk-native \
    "

DEPENDS:append:mx8ulp-generic-bsp = " \
    python3-spsdk-native \
    "

DEPENDS:append:mx8qm-generic-bsp = " \
    python3-spsdk-native \
    "

do_configure:append:mx8m-generic-bsp() {
    # Enable HAB so SPL authenticates the FIT at runtime and the hab_status /
    # hab_auth_img commands are available. With CONFIG_IMX_HAB set, binman signs
    # the SPL and FIT sections at build time via its nxp-imx8mcst node using cst
    # (provided by cst-native), driven by the SRK_TABLE/CSF_KEY/IMG_KEY exports.
    grep -q "^CONFIG_IMX_HAB=y$" "${B}/.config" || echo "CONFIG_IMX_HAB=y" >> "${B}/.config"
}

do_configure:append:mx9-generic-bsp() {
    grep -q "^CONFIG_AHAB_BOOT=y$" "${B}/.config" || echo "CONFIG_AHAB_BOOT=y" >> "${B}/.config"
}

do_configure:append:mx8ulp-generic-bsp() {
    grep -q "^CONFIG_AHAB_BOOT=y$" "${B}/.config" || echo "CONFIG_AHAB_BOOT=y" >> "${B}/.config"
}

do_configure:append:mx8qm-generic-bsp() {
    grep -q "^CONFIG_AHAB_BOOT=y$" "${B}/.config" || echo "CONFIG_AHAB_BOOT=y" >> "${B}/.config"
}

# Signs the imx-boot flash.bin via nxpimage for AHAB platforms (mx9/mx8ulp/mx8qm).
# mx8m (HAB) images are signed by binman/cst at build time instead.
#
# The signer line in spsdk_ahab.yaml is rewritten to a SPSDK SignatureProvider
# config string (type=file;file_path=...;password=...) so nxpimage resolves the
# key through SPSDK's plugin system.  The KMS bbclass overrides this function
# with a type=pkcs11 config string for remote signing.
do_sign_boot_image() {
    if [ ! -e "${SIG_DATA_PATH}/spsdk_ahab.yaml" ]; then
        bbfatal "SPSDK config not found at '${SIG_DATA_PATH}/spsdk_ahab.yaml'. " \
                "Ensure SIG_DATA_PATH is set and points at a PKI tree containing spsdk_ahab.yaml."
    fi

    if [ ! -e "${B}/flash.bin" ]; then
        bbfatal "imx-boot flash.bin is not available to sign"
    fi

    # Build the signer config string for the file-based signature provider.
    KEY_PASS_TXT="${SIG_DATA_PATH}/keys/key_pass.txt"
    if [ -f "${KEY_PASS_TXT}" ]; then
        SIGNER_SED="s|^ *signer: *\(.*\)|signer: type=file;file_path=${SIG_DATA_PATH}/keys/\1;password=${KEY_PASS_TXT}|"
    else
        SIGNER_SED="s|^ *signer: *\(.*\)|signer: type=file;file_path=${SIG_DATA_PATH}/keys/\1|"
    fi

    # Prepare the signing YAML: set family, rewrite signer to config string,
    # and absolutize certificate paths in the SRK array.
    AHAB_SIGN_YAML="${B}/spsdk_ahab_sign.yaml"
    sed -e "s|^ *\(family:\).*|\1 ${SPSDK_FAMILY}|" \
        -e "${SIGNER_SED}" \
        -e "/srk_array/,/^[^ #]/{s|- \([^/][^ ]*\.pem\)|- ${SIG_DATA_PATH}/crts/\1|}" \
        "${SIG_DATA_PATH}/spsdk_ahab.yaml" > "${AHAB_SIGN_YAML}"

    bbnote "AHAB signing flash.bin for ${SPSDK_FAMILY} via nxpimage"

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

# Shared helper: deploy signed-flash.bin, rename original, and create tagged file.
_deploy_signed_flash_bin() {
    install -D -m 0644 -t "${DEPLOYDIR}/" "${B}/signed-flash.bin"
    mv "${DEPLOYDIR}/flash.bin" "${DEPLOYDIR}/unsigned-flash.bin"
    ln -sf "signed-flash.bin" "${DEPLOYDIR}/flash.bin"
    # As per https://github.com/Freescale/meta-freescale/commit/161f1b3e69a3cf011a50e9b742fb8c46d61e41e8, create a tagged file.
    cp -L "${DEPLOYDIR}/flash.bin" "${DEPLOYDIR}/flash.bin.tagged"
    stat -L -cUUUBURNXXOEUZX7+A-XY5601QQWWZ%sEND \
            "${DEPLOYDIR}/flash.bin.tagged" \
            >> "${DEPLOYDIR}/flash.bin.tagged"
    bbnote "AHAB: flash.bin signed successfully for ${SPSDK_FAMILY}"
}

do_deploy:append:mx9-generic-bsp() {
    do_sign_boot_image
}

do_deploy:append:mx8ulp-generic-bsp() {
    do_sign_boot_image
}

do_deploy:append:mx8qm-generic-bsp() {
    do_sign_boot_image
}
