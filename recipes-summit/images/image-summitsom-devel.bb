DESCRIPTION = "Summit SOM Devel Image"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/nomcu:"

inherit image-summitsom-gen image-summitsom-sd-gen

# Data partition size
WIC_ROOTFS_DATA_FIXED_SIZE = "5G"

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-dvk \
    packagegroup-summit-diag \
    packagegroup-core-buildessential \
    "

IMAGE_INSTALL:append:camera = " \
    packagegroup-summit-camera \
    "

include image-summitsom-demo-mcu.inc
