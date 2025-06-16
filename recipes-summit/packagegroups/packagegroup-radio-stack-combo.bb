SUMMARY = "Summit SOM Radio Stack Multi Radio"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-combo-backports \
	60-radio-firmware-sdio-uart \
	60-radio-firmware-sdio-sdio \
	lwb5plus-sdio-sa-firmware \
	lwb5plus-sdio-div-firmware \
	if513-sdio-div-firmware \
	if513-sdio-sa-firmware \
	if573-sdio-firmware \
	nx61x-firmware-1216-serdev \
	nx61x-firmware-1218-serdev \
	ti351-firmware \
	summit-adaptive-ww \
	summit-supplicant \
	summit-supplicant-cli \
	summit-networkmanager \
	summit-networkmanager-nmcli \
	summit-hostapd \
	"
