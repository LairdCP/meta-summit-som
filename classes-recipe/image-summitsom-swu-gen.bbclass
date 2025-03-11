python () {
    image = d.getVar('IMAGE_BASENAME', True)
    type = d.getVar('IMAGE_ROOTFS_VERITY_TYPE', True)
    d.setVarFlag("SWUPDATE_IMAGES_FSTYPES", image, "." + type)
}

inherit swupdate-image

INHIBIT_SWUPDATE_ADD_SRC_URI = "1"

SRC_URI += "file://update_support.sh file://erase_data_emmc.sh file://mksdcard.sh"

SWUPDATE_IMAGES += "\
    ${IMAGE_ROOTFS_VERITY_NAME}.scr.bin \
    ${WORKDIR}/update_support.sh \
    ${WORKDIR}/erase_data_emmc.sh \
    "

ARCHIVE_WILDCARD += "${SWUDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.swu ${WORKDIR}/mksdcard.sh"

do_create_archive[depends] += "${PN}:do_swuimage"

do_create_archive:prepend() {
    install -D -m 0755 -t "${DEPLOY_DIR_IMAGE}" "${WORKDIR}/mksdcard.sh"
}
