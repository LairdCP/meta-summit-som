SUMMARY = "Laird Connectivity TurboSOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

include ../radio-stack-version.inc

inherit packagegroup

RDEPENDS_${PN} = " \
	kernel-module-backports-laird \
	radio-firmware \
	summit-supplicant \
	adaptive-ww \
	laird-networkmanager \
	"