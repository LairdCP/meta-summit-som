FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:imx8mp-summitsom = " \
	file://git \
	file://0001-bsp-integ.patch \
	"

do_deploy:append:mx8m-nxp-bsp() {
	ln -rsf ${DEPLOYDIR}/${UBOOT_DTB_IMAGE} ${DEPLOYDIR}/${BOOT_TOOLS}/${UBOOT_DTB_NAME}
}
