SUMMARY = "Laird Connectivity Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

require radio-stack-som-version.inc

inherit packagegroup

RDEPENDS_${PN} = " \
	mfg60n-som \
	lrd-mfg-scripts-som \
	"