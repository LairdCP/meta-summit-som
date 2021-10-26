SUMMARY = "Laird Connectivity Summit SOM MFG Bridge"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS_${PN} = " \
	mfgbridge-9098 \
	kernel-module-mlan-9098 \
	firmware-mfgbridge-9098 \
	lrd-brg-scripts-9098 \
	"