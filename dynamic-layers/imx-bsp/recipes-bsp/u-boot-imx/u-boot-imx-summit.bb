SUMMARY = "Summit U-Boot for Ezurio boards"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

require recipes-bsp/u-boot/u-boot.inc
require recipes-bsp/u-boot-summit/u-boot-summit-env.inc

inherit summit-platform-version

SRC_URI = "${SUMMIT_EXTERNAL_GIT_URI}/u-boot-som.git;${SUMMIT_EXTERNAL_GIT_SUFFIX}"
SRC_URI:summit-internal = "${SUMMIT_INTERNAL_GIT_URI}/cp_linux-u-boot-som60.git;${SUMMIT_INTERNAL_GIT_SUFFIX}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

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

do_compile[depends] += " \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    "

ATF_MACHINE_NAME ?= "${@bb.utils.contains('MACHINE_FEATURES', 'optee', "bl31-${ATF_PLATFORM}.bin-optee", "bl31-${ATF_PLATFORM}.bin", d)}"

EXTRA_OEMAKE += " \
    BL31=${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} \
    BINMAN_INDIRS=${DEPLOY_DIR_IMAGE} \
    "

EXTRA_OEMAKE += " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', "TEE=${STAGING_LIBDIR}/firmware/tee-pager_v2.bin", '', d)} \
    "

COMPATIBLE_MACHINE = "(imx-generic-bsp)"
