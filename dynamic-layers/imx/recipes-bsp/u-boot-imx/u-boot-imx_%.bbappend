FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0003-Fixed-uboot-environment-saved-every-boot.patch \
    "

SRC_URI:append:summit-secure = " \
    file://u-boot-secure.cfg \
    "

SRC_URI:append:summit-secure-hab = " \
    file://0004-skip-uboot-image-auth.patch \
    file://u-boot-enable-hab.cfg \
    "

SRC_URI:append:wbx3 = " \
    file://wbx3/u-boot-increase-boot-delay.cfg \
    "

SRC_URI:append:imx8mp-summitsom = " \
    file://git \
    file://0001-bsp-integ.patch \
    "

do_deploy:append:mx8m-generic-bsp() {
    ln -rsf "${DEPLOYDIR}/${UBOOT_DTB_IMAGE}" "${DEPLOYDIR}/${BOOT_TOOLS}/${UBOOT_DTB_NAME}"
}

DEPENDS += "u-boot-mkenvimage-native"
DEPENDS:remove:summitsom = "efitools-native gnutls-native"

UBOOT_INITIAL_ENV = "u-boot-initial-env"

# Env base Name
ENV_BASE_NAME ??= "${UBOOT_INITIAL_ENV}-${UBOOT_CONFIG}"

FILES:${PN}-env:append:summitsom = " \
    ${sysconfdir}/fw_env*.config \
    "

do_compile:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        ENV_SIZE=$(sed -rn 's,^CONFIG_ENV_SIZE=(.*),\1,p' "${B}/${config}/.config")
        ENV_OFFSET=$(sed -rn 's,^CONFIG_ENV_OFFSET=(.*),\1,p' "${B}/${config}/.config")

        mkenvimage -s "${ENV_SIZE}" -o "${B}/${config}/${ENV_BASE_NAME}.bin" "${B}/${config}/${ENV_BASE_NAME}"

        echo "/dev/mmcblk${EMMC_DEVICE}boot0 ${ENV_OFFSET} ${ENV_SIZE}" > "${B}/${config}/fw_env_emmc-a.config"
        echo "/dev/mmcblk${EMMC_DEVICE}boot1 ${ENV_OFFSET} ${ENV_SIZE}" > "${B}/${config}/fw_env_emmc-b.config"
        echo "/boot/uboot.env 0 ${ENV_SIZE}" > "${B}/${config}/fw_env_sd.config"
        echo > "${B}/${config}/fw_env.config"
    done
}

do_install:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        install -D -m 644 "${B}/${config}/${ENV_BASE_NAME}.bin" "${D}/boot/uboot.env"
        install -D -m 644 -t "${D}/${sysconfdir}" "${B}/${config}"/fw_env*.config
    done
}

do_deploy:append:summitsom() {
    for config in ${UBOOT_MACHINE}; do
        install -D -m 644 "${B}/${config}/${ENV_BASE_NAME}.bin" "${DEPLOYDIR}/u-boot-${UBOOT_CONFIG}.env"
        ln -sf "u-boot-${UBOOT_CONFIG}.env" "${DEPLOYDIR}/uboot.env"
    done
}
