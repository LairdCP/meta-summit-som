DESCRIPTION = "Summit SOM Command Line Communication Module Image"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/nomcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-diag \
    summit-rcm-rust \
    "
