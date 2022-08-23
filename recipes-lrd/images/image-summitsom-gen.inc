LICENSE = "MIT"

inherit core-image extrausers swupdate-image

EXTRA_USERS_PARAMS = "usermod -P summit root;"

export IMAGE_BASENAME = "${PN}"

SRC_URI += "file://update_support.sh"

IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_FSTYPES = "squashfs-zstd.verity wic.bz2 wic.bmap"

SWUPDATE_IMAGES += "imx-boot fitImage"

do_image_wic[depends] += "${IMAGE_BASENAME}:do_image"

IMAGE_FEATURES = "\
	ssh-server-dropbear \
	allow-root-login \
	"

IMAGE_FEATURES_append_lrdsecure += "\
	read-only-rootfs \
	"

IMAGE_INSTALL_BASIC = "\
	kernel-module-pac193x \
	packagegroup-radio-stack \
	ca-certificates \
	tzdata-core \
	tzdata-posix \
	iproute2 \
	chrony \
	chronyc \
	lrd-automount \
	lrd-initdata \
	lrd-update \
	lrd-fwenv \
	lrd-usbgadget \
	"

# Diagnostic tools
IMAGE_INSTALL_DIAG = "\
	iperf2 \
	iperf3 \
	htop \
	tcpdump \
	usbutils \
	pciutils \
	picocom \
	can-utils \
	stress-ng \
	"
