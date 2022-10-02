FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://0003-ddr-1866MHz.patch \
	file://0004-crypto-fsl_hash-Remove-unnecessary-alignment-check-i.patch \
	"

SRC_URI:append:imx8mp-summitsom = " \
	file://git \
	file://0001-bsp-integ.patch \
	"

