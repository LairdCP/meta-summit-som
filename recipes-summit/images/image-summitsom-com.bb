DESCRIPTION = "Summit SOM Command Line Communication Module Image"

FILESEXTRAPATHS:prepend:mx8mp-nxp-bsp:summitsom := "${THISDIR}/files/nomcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_INSTALL += "\
    packagegroup-summit-basic \
    packagegroup-summit-rcm \
    packagegroup-summit-diag \
    "
