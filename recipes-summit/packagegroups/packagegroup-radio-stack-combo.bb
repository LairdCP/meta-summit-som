SUMMARY = "Summit SOM Radio Stack Multi Radio"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-combo-backports \
	60-radio-firmware-sdio-uart \
	if513-sdio-div-firmware \
	if573-sdio-firmware \
	nx61x-firmware \
	ti351-firmware \
	summit-adaptive-ww \
	summit-supplicant-60 \
	summit-supplicant-60-cli \
	summit-networkmanager-60 \
	summit-networkmanager-60-nmcli \
	summit-hostapd-60 \
	"
