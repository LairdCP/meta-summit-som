DESCRIPTION = "Summit SOM WBx3 Image"

REQUIRED_DISTRO_FEATURES:append = " \
    summitsom-wbx3 \
    "

FILESEXTRAPATHS:prepend := "\
${SUMMIT_SOM_LAYERDIR}/recipes-summit/images/files/nomcu:\
${SUMMIT_SOM_LAYERDIR}/recipes-summit/images/files:\
"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

FITIMAGE_INITRAMFS_LINK ?= "fitImage-initramfs"

ARCHIVE_WILDCARD:append:summitsom-wbx3-initramfs = " \
    ${DEPLOY_DIR_IMAGE}/flash-initramfs.bin \
    ${DEPLOY_DIR_IMAGE}/${FITIMAGE_INITRAMFS_LINK}"

IMAGE_POSTPROCESS_COMMAND:append:summitsom-wbx3-initramfs = " copy_initramfs_deploy_artifacts;"

copy_initramfs_deploy_artifacts() {
    cp -fL "${DEPLOY_DIR_IMAGE}/flash.bin" "${DEPLOY_DIR_IMAGE}/flash-initramfs.bin"
    cp -fL "${DEPLOY_DIR_IMAGE}/fitImage-image-${DISTRO}-${MACHINE}-${MACHINE}" "${DEPLOY_DIR_IMAGE}/${FITIMAGE_INITRAMFS_LINK}"
}
