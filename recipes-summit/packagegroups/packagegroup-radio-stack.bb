SUMMARY = "Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-60-backports \
	som8mp-radio-firmware \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'adaptive-bt bt-uart-scripts', '', d)} \
	summit-supplicant-libs-60 \
	summit-supplicant-60 \
	summit-supplicant-60-cli \
	adaptive-ww \
	summit-networkmanager-60 \
	summit-networkmanager-60-nmcli \
	"