SUMMARY = "Laird Connectivity Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

include ../radio-stack-version.inc

inherit packagegroup

RDEPENDS_${PN} = " \
	mfg60n \
	mfg60n-lrt \
	radio-firmware-st \
	lrd-mfg-scripts \
	"