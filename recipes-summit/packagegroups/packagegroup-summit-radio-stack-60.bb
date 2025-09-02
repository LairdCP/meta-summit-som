SUMMARY = "Summit SOM Radio Stack 60 Radio"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-60-backports \
	60-radio-firmware-sdio-uart \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'summit-adaptive-bt summit-bt-uart-scripts-60', '', d)} \
	summit-supplicant-libs \
	summit-supplicant \
	summit-supplicant-cli \
	summit-adaptive-ww \
	summit-networkmanager \
	summit-networkmanager-nmcli \
	"

RDEPENDS:${PN}:imx8mp-summitsom:append = " som8mp-radio-firmware"
RDEPENDS:${PN}:imx8mp-summitsom:remove = "60-radio-firmware-sdio-uart"
