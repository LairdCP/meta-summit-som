DESCRIPTION = "Summit SOM Weston Image"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/nomcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES += "splash hwcodecs weston"

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-dvk \
    packagegroup-summit-diag \
    "

include image-summitsom-demo-mcu.inc
