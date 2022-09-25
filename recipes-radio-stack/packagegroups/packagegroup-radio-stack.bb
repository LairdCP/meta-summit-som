SUMMARY = "Laird Connectivity Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

require radio-stack-som-version.inc

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-som-backports-laird \
	som8mp-radio-firmware \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'adaptive-bt-som lrd-bt-uart-scripts-som', '', d)} \
	summit-supplicant-som \
	summit-supplicant-som-cli \
	adaptive-ww-som \
	lrd-networkmanager-som \
	lrd-networkmanager-som-nmcli \
	iptables \
	"