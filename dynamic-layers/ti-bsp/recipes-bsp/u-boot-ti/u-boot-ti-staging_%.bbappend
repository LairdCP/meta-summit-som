FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/${PN}:"

SRC_URI:append:summitsom = " \
    file://0001-carbon-am62l-uboot.patch \
    "

export KEY_PATH = "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key"

require recipes-bsp/u-boot-summit/u-boot-summit-env.inc
