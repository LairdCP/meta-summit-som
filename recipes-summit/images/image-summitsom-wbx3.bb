DESCRIPTION = "Summit SOM WBx3 Image"

REQUIRED_DISTRO_FEATURES:append = " \
    summitsom-wbx3 \
    "

FILESEXTRAPATHS:prepend := "\
${SUMMIT_SOM_LAYERDIR}/recipes-summit/images/files/nomcu:\
${SUMMIT_SOM_LAYERDIR}/recipes-summit/images/files:\
"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

FITIMAGE_INITRAMFS_LINK ?= "fitImage-initramfs-${MACHINE}"
