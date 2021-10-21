SUMMARY = "Laird Connectivity Summit SOM MFG Bridge"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS_${PN} = " \
	mfgbridge-8997 \
	kernel-module-mlan-8997-pcie \
	kernel-module-mlan-8997-sdio \
	firmware-mfgbridge-8997 \
	radio-firmware-st \
	mfg60n-lrt \
	"