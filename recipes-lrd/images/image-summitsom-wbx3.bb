DESCRIPTION = "Summit SOM WBx3 Line Image"

LICENSE = "MIT"

inherit core-image

export IMAGE_BASENAME = "${PN}"

IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_FSTYPES = "squashfs-zstd.verity wic.bz2 wic.bmap"

do_image_wic[depends] += "${IMAGE_BASENAME}:do_image"

IMAGE_FEATURES = "\
	ssh-server-dropbear \
	allow-root-login \
	"

IMAGE_INSTALL = "\
	iproute2 \
	lrd-initdata \
	lrd-update \
	lrd-fwenv \
	u-boot-fw-utils \
	swupdate \
	swupdate-client \
	keyctl-caam \
	crypto-af-alg \
	keyutils \
	lvm2 \
	mmc-utils \
	"
