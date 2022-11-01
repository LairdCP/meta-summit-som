IMAGE_FSTYPES += "wic.bz2 wic.bmap"

WIC_ROOTFS_FIXED_SIZE ?= "512M"
WIC_ROOTFS_DATA_FIXED_SIZE ?= "1G"

WKS_FILES = "summit-imx-uboot-spl-bootpart.wks.in"

ARCHIVE_WILDCARD += "${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.wic.* ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic.*"
