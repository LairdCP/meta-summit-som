FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0003-Fixed-uboot-environment-saved-every-boot.patch \
    "

SRC_URI:append:imx8mp-summitsom = " \
    file://git \
    file://0001-bsp-integ.patch \
    "

do_deploy:append:mx8m-nxp-bsp() {
    ln -rsf "${DEPLOYDIR}/${UBOOT_DTB_IMAGE}" "${DEPLOYDIR}/${BOOT_TOOLS}/${UBOOT_DTB_NAME}"
}

DEPENDS += "u-boot-mkenvimage-native"

UBOOT_INITIAL_ENV = "u-boot-initial-env"

# Env binary size
ENV_SIZE = "0x4000"

# Env base Name
ENV_BASE_NAME ??= "${UBOOT_INITIAL_ENV}-${UBOOT_CONFIG}"

do_compile:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        mkenvimage -s "${ENV_SIZE}" -o "${B}/${config}/${ENV_BASE_NAME}.bin" "${B}/${config}/${ENV_BASE_NAME}"
    done
}

do_install:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        install -D -m 644 "${B}/${config}/${ENV_BASE_NAME}.bin" "${D}/boot/u-boot.env"
    done
}

do_deploy:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        install -D -m 644 "${B}/${config}/${ENV_BASE_NAME}.bin" "${DEPLOYDIR}/u-boot-${UBOOT_CONFIG}.env"
        ln -sf "u-boot-${UBOOT_CONFIG}.env" "${DEPLOYDIR}/u-boot.env"
    done
}
