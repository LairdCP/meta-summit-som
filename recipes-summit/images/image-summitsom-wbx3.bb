DESCRIPTION = "Summit SOM WBx3 Line Image"

FILESEXTRAPATHS:prepend:mx8mp-nxp-bsp:summitsom := "${THISDIR}/files/nomcu:"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES += "\
	allow-empty-password \
	empty-root-password \
	"

EXTRA_USERS_PARAMS = ""

IMAGE_INSTALL += "\
	iproute2 \
	summit-initdata \
	summit-update \
	less \
	busybox-udhcpc \
	"

ROOTFS_POSTPROCESS_COMMAND += "rootfs_auto_login; "

rootfs_auto_login() {
	sed -i -e 's,/agetty,/agetty -a root,g' "${IMAGE_ROOTFS}${systemd_unitdir}/system/serial-getty@.service"
}
