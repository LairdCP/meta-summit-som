inherit swupdate-image

# This file is included by recipes in other layers
# Add to files fetcher search path
FILESEXTRAPATHS:append := ":${THISDIR}/files"
SRC_URI += "file://update_support.sh"

SWUPDATE_IMAGES += "imx-boot ${IMAGE_BOOT_FILES}"

python () {
    image = d.getVar('IMAGE_LINK_NAME', True)
    d.setVarFlag("SWUPDATE_IMAGES_FSTYPES", image, ".squashfs-zstd.verity")

    if d.getVar('SUMMIT_VERSION') != '0.0.0.0':
       bb.build.addtask('create_archive', 'do_build', 'do_swuimage', d)
}

do_create_archive:append() {
	cd ${SWUDEPLOYDIR}
	tar -rf ${DEPLOY_DIR_IMAGE}/${ARCHIVE_NAME}.tar \
		${IMAGE_NAME}.swu ${IMAGE_LINK_NAME}.swu
}