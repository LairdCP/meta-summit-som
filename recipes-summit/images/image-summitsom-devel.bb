DESCRIPTION = "Summit SOM Devel Image"

FILESEXTRAPATHS:prepend:use-nxp-bsp:summitsom := "${THISDIR}/files/nomcu:"

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

include image-summitsom-demo-mcu.inc
