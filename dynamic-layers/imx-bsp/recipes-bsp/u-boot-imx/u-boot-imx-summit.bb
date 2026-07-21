SUMMARY = "Summit U-Boot for Ezurio boards"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

inherit summit-platform-version use-imx-security-controller-firmware

require recipes-bsp/u-boot/u-boot.inc
require recipes-bsp/u-boot-summit/u-boot-summit-env.inc

SRC_URI = "${SUMMIT_EXTERNAL_GIT_URI}/u-boot-som.git;${SUMMIT_EXTERNAL_GIT_SUFFIX}"
SRC_URI:summit-internal = "${SUMMIT_INTERNAL_GIT_URI}/cp_linux-u-boot-som60.git;${SUMMIT_INTERNAL_GIT_SUFFIX}"
SRC_URI:append:mx9-generic-bsp = " file://0002-ele-ahab-include-imx-regs-for-IMG_CONTAINER_BASE.patch"
SRC_URI:append:summit-secure:mx95-generic-bsp = " file://0001-imx8image-Set-signature-block-version-1-for-AHAB-V2.patch"

SRC_URI:append:summitsom-wbx3-initramfs = " \
    file://uboot-initramfs.cfg \
    file://uboot-initramfs.env \
"

S = "${UNPACKDIR}/git"
B = "${UNPACKDIR}/build"

PROVIDES += "u-boot"

DEPENDS += "\
    flex-native \
    bison-native \
    python3-setuptools-native \
    python3-pyelftools-native \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
    "

# AHAB secure boot: SPSDK family mapping and signing tool dependency.
SPSDK_FAMILY:mx93-generic-bsp = "mimx9352"
SPSDK_FAMILY:mx95-generic-bsp = "mimx9596"

DEPENDS:append:mx9-generic-bsp:summit-secure = " python3-spsdk-native"

do_compile[depends] += " \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    "

ATF_MACHINE_NAME ?= "${@bb.utils.contains('MACHINE_FEATURES', 'optee', "bl31-${ATF_PLATFORM}.bin-optee", "bl31-${ATF_PLATFORM}.bin", d)}"

EXTRA_OEMAKE += " \
    BL31=${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} \
    BINMAN_INDIRS=${DEPLOY_DIR_IMAGE} \
    BINMAN_VERBOSE=3 \
    "

EXTRA_OEMAKE += " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', "TEE=${STAGING_LIBDIR}/firmware/tee-pager_v2.bin", '', d)} \
    "

python do_patch:append () {
    bb.build.exec_func('do_copy_board_files', d)
}

do_copy_board_files () {
    # placeholder for machine-specific board file copying, called from do_patch for relevant
    # machines
    true; 
}

do_copy_board_files:append:summitsom-wbx3-initramfs () {
    board_dir="${UBOOT_MACHINE}"
    board_dir="${board_dir%_defconfig}"
    install -d "${S}/board/summit/${board_dir}"
    install -m 0644 "${UNPACKDIR}/uboot-initramfs.env" \
        "${S}/board/summit/${board_dir}/uboot-initramfs.env"
}

do_compile:prepend:mx9-generic-bsp() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}; then
        ln -sf "${STAGING_LIBDIR}/firmware/tee-pager_v2.bin" "${B}/tee.bin"
    fi
    ln -sf "${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}" "${B}/bl31.bin"
    ln -sf "${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}" "${B}/${SECO_FIRMWARE_NAME}"
}

do_compile:prepend:mx95-generic-bsp() {
    ln -sf "${DEPLOY_DIR_IMAGE}/${SYSTEM_MANAGER_FIRMWARE_NAME}.bin" "${B}/m33_image.bin"
}

# --- AHAB secure boot signing (nxpimage / SPSDK) ------------------------------
# flash.bin is signed by calling nxpimage (SPSDK) directly. The older
# nxp-imx-signer (imx_signer) wrapper is intentionally not used: it is
# incompatible with spsdk >= 3.7.0 (it strips the .bin extension and passes a
# non-existent path to nxpimage's -b/--binary option).

do_configure:append:mx9-generic-bsp:summit-secure() {
    if [ ! -f "${B}/.config" ]; then
        bbfatal "u-boot .config not found at '${B}/.config'; cannot enable CONFIG_AHAB_BOOT"
    fi
    grep -q "^CONFIG_AHAB_BOOT=y$" "${B}/.config" || echo "CONFIG_AHAB_BOOT=y" >> "${B}/.config"
}

# Signs the imx-boot flash.bin via nxpimage for AHAB platforms. The signer line
# in spsdk_ahab.yaml is rewritten to a SPSDK SignatureProvider config string
# (type=file;file_path=...;password=...) so nxpimage resolves the key through
# SPSDK's plugin system, and SRK certificate paths are absolutized.
do_sign_boot_image() {
    if [ ! -e "${SIG_DATA_PATH}/spsdk_ahab.yaml" ]; then
        bbfatal "SPSDK config not found at '${SIG_DATA_PATH}/spsdk_ahab.yaml'. Ensure SIG_DATA_PATH points at a PKI tree containing spsdk_ahab.yaml."
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
    CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1 \
    "${STAGING_BINDIR_NATIVE}/nxpimage" ahab sign \
        -c "${AHAB_SIGN_YAML}" \
        -b "${B}/flash.bin" \
        -o "${B}/signed-flash.bin" \
        --force

    if [ ! -e "${B}/signed-flash.bin" ]; then
        bbfatal "AHAB signing failed -- signed-flash.bin was not produced"
    fi
    rm -f "${AHAB_SIGN_YAML}"
}

do_deploy:append:mx9-generic-bsp:summit-secure() {
    do_sign_boot_image

    # Copy signed image to DEPLOYDIR and link it to boot image
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

EXTRA_OEMAKE:append:summit-secure = " KEY_PATH=${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key"

COMPATIBLE_MACHINE = "(imx-generic-bsp)"
