IMAGE_FSTYPES += "wic.bz2 wic.bmap"

WIC_ROOTFS_FIXED_SIZE ?= "512M"
WIC_ROOTFS_DATA_FIXED_SIZE ?= "1G"

WKS_FILES:imx-nxp-bsp = "summit-imx-uboot-spl-bootpart.wks.in"
WKS_FILES:ti-soc = "summit-ti-uboot-spl-bootpart.wks.in"

WIC_IMAGE_PATH = "${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.wic.*"

ARCHIVE_WILDCARD += "${@ d.getVar('WIC_IMAGE_PATH') if not d.getVar('SWUDEPLOYDIR') or d.getVar('WIC_IMAGE_FORCE_ARCHIVE') else '' }"
