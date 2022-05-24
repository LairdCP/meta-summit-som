SUMMARY = "Laird Connectivity Summit SOM Manufacturing Bridge Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

require radio-stack-som-version.inc

inherit packagegroup

RDEPENDS_${PN} = " \
	kernel-module-som-backports-brg \
	summit60-firmware-som \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'adaptive-bt-som lrd-bt-uart-scripts-som', '', d)} \
	summit-supplicant-som \
	summit-supplicant-som-cli \
	adaptive-ww-som \
	lrd-networkmanager-som \
	lrd-networkmanager-som-nmcli \
	iptables \
	mfg60n-som \
	lrd-mfg-scripts-som \
	"