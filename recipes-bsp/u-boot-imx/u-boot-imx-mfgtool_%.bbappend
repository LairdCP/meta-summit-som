FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://git \
	file://0001-bsp-integ.patch \
	file://0003-ddr-1866MHz.patch \
	file://0004-crypto-fsl_hash-Remove-unnecessary-alignment-check-i.patch \
	"
