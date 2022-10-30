LICENSE = "MIT"

inherit core-image extrausers swupdate-image

PASSWD = "\$5\$JJ/ksbVr4475qA49\$wzyEBumoH1YyHOG9OgKzjRKjwGVImxFDCu0m90hymoA"

EXTRA_USERS_PARAMS = "usermod -p '${PASSWD}' root;"

export IMAGE_BASENAME = "${PN}"

# This file is included by recipes in other layers
# Add to files fetcher search path
FILESEXTRAPATHS:append := ":${THISDIR}/files"
SRC_URI += "file://update_support.sh"

IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_FSTYPES = "squashfs-zstd.verity wic.zst wic.bmap"

SWUPDATE_IMAGES += "imx-boot ${IMAGE_BOOT_FILES}"

WIC_ROOTFS_FIXED_SIZE ?= "512M"
WIC_ROOTFS_DATA_FIXED_SIZE ?= "1G"

WKS_FILES = "summit-imx-uboot-spl-bootpart.wks.in"

IMAGE_FEATURES = "\
	ssh-server-dropbear \
	allow-root-login \
	"

IMAGE_FEATURES:append:lrdsecure = "\
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
	summit-automount \
	summit-initdata \
	summit-update \
	summit-fwenv \
	summit-usbgadget \
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

ROOTFS_POSTPROCESS_COMMAND += "rootfs_os_release; "

SUMMIT_VERSION ?= "0.0.0.0"

rootfs_os_release() {
	sed -i -e 's,ID=os-release,ID=${IMAGE_BASENAME},g' ${IMAGE_ROOTFS}${libdir}/os-release
	echo 'Summit SOM ${IMAGE_BASENAME} ${SUMMIT_VERSION} \\n \l' > ${IMAGE_ROOTFS}${sysconfdir}/issue
	echo 'Summit SOM ${IMAGE_BASENAME} ${SUMMIT_VERSION} %h' > ${IMAGE_ROOTFS}${sysconfdir}/issue.net
}
