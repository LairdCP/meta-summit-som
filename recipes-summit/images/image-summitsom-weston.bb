DESCRIPTION = "Summit SOM Weston Image"

FILESEXTRAPATHS:prepend:use-nxp-bsp:summitsom := "${THISDIR}/files/mcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES += "splash hwcodecs weston"

CORE_IMAGE_EXTRA_INSTALL += "\
    ${IMAGE_INSTALL_BASIC} \
    ${IMAGE_INSTALL_DIAG} \
    mdio-tools \
    linuxptp \
    systemd-analyze \
    alsa-utils-alsamixer \
    alsa-utils-aplay \
    alsa-utils-speakertest \
    v4l-utils \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base-meta \
    gstreamer1.0-plugins-good-meta \
    gstreamer1.0-plugins-bad-meta \
    ${@bb.utils.contains('LICENSE_FLAGS_ACCEPTED', 'commercial', 'gstreamer1.0-libav', '', d)} \
    "

CORE_IMAGE_EXTRA_INSTALL:append:imx8mp-summitsom = " \
    packagegroup-radio-stack-60 \
    kernel-module-pac193x \
    summit-mcu-demos \
    qfirehose \
    "

IMAGE_BOOT_FILES:append:imx8mp-summitsom = " fitImageMcu.bin"
SWUPDATE_IMAGES:append:imx8mp-summitsom = " fitImageMcu.bin"
WKS_FILE_DEPENDS:append:imx8mp-summitsom = " summit-mcu-demos"

CORE_IMAGE_EXTRA_INSTALL:append:k3:summitsom = " \
    packagegroup-radio-stack-lwb-if \
    kernel-module-tac5x1x \
    "
