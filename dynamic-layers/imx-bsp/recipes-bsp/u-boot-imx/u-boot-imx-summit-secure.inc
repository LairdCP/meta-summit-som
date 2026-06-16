SIG_CFGFILE = "sign.cfg"

# SPSDK family name mapping for AHAB devices
SPSDK_FAMILY:mx93-generic-bsp = "mimx9352"
SPSDK_FAMILY:mx95-generic-bsp = "mimx9596"

SRC_URI:append:mx95-generic-bsp = " 
    file://0001-imx8image-Set-signature-block-version-1-for-AHAB-V2.patch \
    "

DEPENDS += "nxp-imx-signer-native"

DEPENDS:append:mx9-generic-bsp = " \
    python3-spsdk-native \
    "

do_compile:prepend:mx9-generic-bsp() {
    # Update defconfig to enable secure boot
    echo "CONFIG_AHAB_BOOT=y" >> "${B}/.config"
}

# Signs the imx-boot image. This command assumes that the PKI tree was generated.
do_sign_boot_image() {
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
}

do_sign_boot_image:prepend:mx9-generic-bsp() {
    # Creating a cfg file for imx_signer
    if [ -e "${SIG_DATA_PATH}/spsdk_ahab.yaml" ]; then
        # Use user defined keys
        install -D -m 0644 "${SIG_DATA_PATH}/spsdk_ahab.yaml" "${B}/${SIG_CFGFILE}"
    else
        # Use default keys
        install -D -m 0644 "${STAGING_DIR_NATIVE}${datadir}/spsdk_ahab.yaml.sample" "${B}/${SIG_CFGFILE}"
    fi

    bbnote "Setting SPSDK family to: ${SPSDK_FAMILY}, in ${SIG_CFGFILE} file"
    sed -i "s/^family:.*/family: ${SPSDK_FAMILY}/" "${B}/${SIG_CFGFILE}"
}

do_deploy:append:mx9-generic-bsp() {
    do_sign_boot_image

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
