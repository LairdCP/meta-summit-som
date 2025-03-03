require recipes-bsp/u-boot/u-boot-ti.inc
require summit-platform-version.inc

SUMMARY = "Summit U-Boot for TI devices"

UBOOT_GIT_URI = "git://github.com/Ezurio/u-boot-som.git"
UBOOT_GIT_URI:summit-internal = "git://git@github.com/rfpros/cp_linux-u-boot-som60.git"

UBOOT_GIT_PROTOCOL:summit-internal = "ssh"
UBOOT_GIT_BRANCH = "nobranch=1"

SRCREV = "master"
PV = "master+git${SRCPV}"
#SRCREV = "${SUMMIT_PLATFORM_VERSION}"
#PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS:append:k3 = " u-boot-mkenvimage-native"

UBOOT_INITIAL_ENV:k3 = "u-boot-initial-env"

FILES:${PN}-env:append:k3:summitsom = " \
    ${sysconfdir}/fw_env*.config \
    "

do_compile:append:k3:summitsom() {
    ENV_SIZE=$(sed -rn 's,^CONFIG_ENV_SIZE=(.*),\1,p' "${B}/.config")
    ENV_OFFSET=$(sed -rn 's,^CONFIG_ENV_OFFSET=(.*),\1,p' "${B}/.config")

    mkenvimage -s "${ENV_SIZE}" -o "${B}/${UBOOT_INITIAL_ENV}.bin" "${B}/${UBOOT_INITIAL_ENV}"

    echo "/dev/mmcblk${EMMC_DEVICE}boot0 ${ENV_OFFSET} ${ENV_SIZE}" > "${B}/fw_env_emmc-a.config"
    echo "/dev/mmcblk${EMMC_DEVICE}boot1 ${ENV_OFFSET} ${ENV_SIZE}" > "${B}/fw_env_emmc-b.config"
    echo "/boot/uboot.env 0 ${ENV_SIZE}" > "${B}/fw_env_sd.config"
    echo > "${B}/fw_env.config"
}

do_install:append:k3:summitsom() {
    install -D -m 644 "${B}/${UBOOT_INITIAL_ENV}.bin" "${D}/boot/uboot.env"
    install -D -m 644 -t "${D}/${sysconfdir}" "${B}"/fw_env*.config
}

do_deploy:append:k3:summitsom() {
    install -D -m 644 "${B}/${UBOOT_INITIAL_ENV}.bin" "${DEPLOYDIR}/u-boot-${UBOOT_CONFIG}.env"
    ln -sf "u-boot-${UBOOT_CONFIG}.env" "${DEPLOYDIR}/uboot.env"
}
