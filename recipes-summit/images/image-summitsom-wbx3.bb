DESCRIPTION = "Summit SOM WBx3 Line Image"

inherit image-summitsom-gen image-summitsom-sd-gen

IMAGE_FEATURES += "\
	allow-empty-password \
	empty-root-password \
	"

CORE_IMAGE_EXTRA_INSTALL += "\
	iproute2 \
	summit-initdata \
	summit-update \
	summit-fwenv \
	less \
	"

ROOTFS_POSTPROCESS_COMMAND += "rootfs_auto_login; "

rootfs_auto_login() {
	sed -i -e 's,/agetty,/agetty -a root,g' ${IMAGE_ROOTFS}${systemd_unitdir}/system/serial-getty@.service
}
