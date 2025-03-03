SUMMARY = "Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

SOM8MP_RADIO_STACK = "0"
SOM8MP_RADIO_STACK:imx8mp-summitsom = "1"

RDEPENDS:${PN} = " \
	kernel-module-60-backports \
	${@bb.utils.contains('SOM8MP_RADIO_STACK', '1', 'som8mp-radio-firmware', '60-radio-firmware-sdio-uart', d)} \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'summit-adaptive-bt summit-bt-uart-scripts-60', '', d)} \
	summit-supplicant-libs-60 \
	summit-supplicant-60 \
	summit-supplicant-60-cli \
	summit-adaptive-ww \
	summit-networkmanager-60 \
	summit-networkmanager-60-nmcli \
	"
