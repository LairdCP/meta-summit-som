SIG_CFGFILE = "sign.cfg"

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
    nxp-imx-signer-native \
    python3-spsdk-native \
    "

DEPENDS:append:mx8ulp-generic-bsp = " \
    nxp-imx-signer-native \
    python3-spsdk-native \
    "

DEPENDS:append:mx8qm-generic-bsp = " \
    nxp-imx-signer-native \
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

# Signs the imx-boot image. This command assumes that the PKI tree was generated.
# Used by the AHAB (mx9) path; mx8 (HAB) images are signed by binman at build time.
do_sign_boot_image() {
    if [ ! -e "${SIG_DATA_PATH}/spsdk_ahab.yaml" ]; then
        bbfatal "SPSDK config not found at '${SIG_DATA_PATH}/spsdk_ahab.yaml'. "\
        "Ensure SIG_DATA_PATH is set and points at a PKI tree containing spsdk_ahab.yaml."
    fi
    install -D -m 0644 "${SIG_DATA_PATH}/spsdk_ahab.yaml" "${B}/${SIG_CFGFILE}"

    bbnote "Setting SPSDK family to: ${SPSDK_FAMILY}, in ${SIG_CFGFILE} file"
    sed -i "s/^family:.*/family: ${SPSDK_FAMILY}/" "${B}/${SIG_CFGFILE}"

    # Check if flash.bin is available
    if [ ! -e "${B}/flash.bin" ]; then
        bbfatal 'imx-boot flash.bin is not available to sign'
    fi

    # Generate signed image using imx_signer
    cd "${B}" || bbfatal "Failed to change directory to ${B}"
    SIG_TOOL_PATH="${STAGING_DIR_NATIVE}${bindir}" \
        SIG_DATA_PATH=${SIG_DATA_PATH} \
        CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1 \
        "${STAGING_DIR_NATIVE}${bindir}/imx_signer" \
        -d \
        -i "${B}/flash.bin" \
        -c "${B}/${SIG_CFGFILE}"
    if [ ! -e "${B}/signed-flash.bin" ]; then
        bbfatal 'Image signing failed'
    fi

    cd - || bbfatal "Failed to change back to previous directory"

    # Copy signed image to DEPLOYDIR and link it to boot image
    if [ -e "${B}/signed-flash.bin" ]; then
        install -D -m 0644 -t "${DEPLOYDIR}/" "${B}/signed-flash.bin"
        mv "${DEPLOYDIR}/flash.bin" "${DEPLOYDIR}/unsigned-flash.bin"
        ln -sf "signed-flash.bin" "${DEPLOYDIR}/flash.bin"
        # As per https://github.com/Freescale/meta-freescale/commit/161f1b3e69a3cf011a50e9b742fb8c46d61e41e8, create a tagged file.
        cp -L "${DEPLOYDIR}/flash.bin" "${DEPLOYDIR}/flash.bin.tagged"
        stat -L -cUUUBURNXXOEUZX7+A-XY5601QQWWZ%sEND \
                "${DEPLOYDIR}/flash.bin.tagged" \
                >> "${DEPLOYDIR}/flash.bin.tagged"
    else
        bbfatal "ERROR: Could not deploy Signed image"
    fi
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
