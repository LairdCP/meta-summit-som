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
	allow-empty-password \
	empty-root-password \
	"

CORE_IMAGE_EXTRA_INSTALL += "\
	iproute2 \
	lrd-initdata \
	lrd-update \
	lrd-fwenv \
	less \
	"

ROOTFS_POSTPROCESS_COMMAND += "rootfs_auto_login; rootfs_os_release; "

rootfs_auto_login() {
	sed -i -e 's,/agetty,/agetty -a root,g' ${IMAGE_ROOTFS}${systemd_unitdir}/system/serial-getty@.service
}

LRD_VERSION ?= "0.0.0.0"

rootfs_os_release() {
	sed -i -e 's,ID=os-release,ID=${IMAGE_BASENAME},g' ${IMAGE_ROOTFS}${libdir}/os-release
	echo 'Laird Connectivity Summit SOM ${IMAGE_BASENAME} ${LRD_VERSION} \\n \l' > ${IMAGE_ROOTFS}${sysconfdir}/issue
	echo 'Laird Connectivity Summit SOM ${IMAGE_BASENAME} ${LRD_VERSION} %h' > ${IMAGE_ROOTFS}${sysconfdir}/issue.net
}
