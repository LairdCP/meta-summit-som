FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:imx8mp-summitsom = " \
	file://git \
	file://0002-bsp-integ-tools.patch \
	"
