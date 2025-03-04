DESCRIPTION = "Summit SOM Command Line Minimal Image"

FILESEXTRAPATHS:prepend:use-nxp-bsp:summitsom := "${THISDIR}/files/mcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

CORE_IMAGE_EXTRA_INSTALL += "\
    ${IMAGE_INSTALL_BASIC} \
    ${IMAGE_INSTALL_DIAG} \
    mdio-tools \
    linuxptp \
    systemd-analyze \
    summit-rcm \
    summit-rcm-chrony-plugin \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'summit-rcm-bluetooth-plugin', '', d)} \
    "

CORE_IMAGE_EXTRA_INSTALL:append:imx8mp-summitsom = " \
    summit-rcm-awm-plugin \
    packagegroup-radio-stack-60 \
    kernel-module-pac193x \
    summit-mcu-demos \
    qfirehose \
    "

IMAGE_BOOT_FILES:append:imx8mp-summitsom = " fitImageMcu.bin"
SWUPDATE_IMAGES:append:imx8mp-summitsom = " fitImageMcu.bin"
WKS_FILE_DEPENDS:append:imx8mp-summitsom = " summit-mcu-demos"

CORE_IMAGE_EXTRA_INSTALL:append:am62xx-carbon = " \
    packagegroup-radio-stack-lwb-if \
    kernel-module-tac5x1x \
    "
