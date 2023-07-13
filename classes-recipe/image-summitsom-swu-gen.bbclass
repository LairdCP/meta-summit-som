
python () {
    image = d.getVar('IMAGE_BASENAME', True)
    d.setVarFlag("SWUPDATE_IMAGES_FSTYPES", image, ".squashfs-zstd.verity")
}

inherit swupdate-image

SRC_URI += "file://update_support.sh file://erase_data.sh"

SWUPDATE_IMAGES += "imx-boot ${IMAGE_BOOT_FILES}"

ARCHIVE_WILDCARD += "${SWUDEPLOYDIR}/${IMAGE_NAME}.swu ${SWUDEPLOYDIR}/${IMAGE_LINK_NAME}.swu"

addtask create_archive after do_swuimage before do_build
