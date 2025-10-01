DESCRIPTION = "Summit SOM Devel Image"

FILESEXTRAPATHS:prepend:mx8mp-generic-bsp:summitsom := "${THISDIR}/files/mcu:"

inherit image-summitsom-gen image-summitsom-sd-gen

# Data partition size
WIC_ROOTFS_DATA_FIXED_SIZE = "5G"

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-dvk \
    packagegroup-summit-diag \
    packagegroup-summit-camera \
    packagegroup-core-buildessential \
    "

IMAGE_INSTALL:append:imx8mp-summitsom = "summit-mcu-demos"
IMAGE_BOOT_FILES:append:imx8mp-summitsom = " fitImageMcu.bin"
SWUPDATE_IMAGES:append:imx8mp-summitsom = " fitImageMcu.bin"
WKS_FILE_DEPENDS:append:imx8mp-summitsom = " summit-mcu-demos"
