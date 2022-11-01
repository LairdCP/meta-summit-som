IMAGE_FSTYPES += "wic.bz2 wic.bmap"

WIC_ROOTFS_FIXED_SIZE ?= "512M"
WIC_ROOTFS_DATA_FIXED_SIZE ?= "1G"

WKS_FILES = "summit-imx-uboot-spl-bootpart.wks.in"

do_create_archive:append() {
	cd ${IMGDEPLOYDIR}
	tar -rf ${DEPLOY_DIR_IMAGE}/${ARCHIVE_NAME}.tar \
		${IMAGE_NAME}.rootfs.wic.* ${IMAGE_LINK_NAME}.wic.*
}