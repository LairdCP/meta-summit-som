FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_imx8mp-summitsom = " \
	file://git \
	file://0002-bsp-integ-tools.patch \
	"

