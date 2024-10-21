python () {
    image = d.getVar('IMAGE_BASENAME', True)
    type = d.getVar('IMAGE_ROOTFS_VERITY_TYPE', True)
    d.setVarFlag("SWUPDATE_IMAGES_FSTYPES", image, "." + type)
}

inherit swupdate-image

SRC_URI += "file://update_support.sh file://erase_data.sh"

SWUPDATE_IMAGES += "${IMAGE_ROOTFS_VERITY_NAME}.scr.bin"

ARCHIVE_WILDCARD += "${SWUDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.swu"

addtask create_archive after do_swuimage before do_build
