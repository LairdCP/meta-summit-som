DESCRIPTION = "Summit SOM Weston Image"

FILESEXTRAPATHS:prepend:use-nxp-bsp:summitsom := "${THISDIR}/files/mcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES += "splash hwcodecs weston"

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-dvk \
    packagegroup-summit-diag \
    packagegroup-summit-camera \
    "

IMAGE_INSTALL:append:imx8mp-summitsom = "summit-mcu-demos"
IMAGE_BOOT_FILES:append:imx8mp-summitsom = " fitImageMcu.bin"
SWUPDATE_IMAGES:append:imx8mp-summitsom = " fitImageMcu.bin"
WKS_FILE_DEPENDS:append:imx8mp-summitsom = " summit-mcu-demos"
