SUMMARY = "Summit SOM Radio Stack Multi Radio"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-combo-backports \
	60-radio-firmware-sdio-uart \
	60-radio-firmware-sdio-sdio \
	${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', '60-radio-firmware-usb-usb', '', d)} \
	${@bb.utils.contains('MACHINE_FEATURES', 'pci', '60-radio-firmware-pcie-uart', '', d)} \
	lwb5plus-sdio-sa-firmware \
	lwb5plus-sdio-div-firmware \
	${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', 'lwb5plus-usb-div-firmware', '', d)} \
	${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', 'lwb5plus-usb-sa-firmware', '', d)} \
	if513-sdio-div-firmware \
	if513-sdio-sa-firmware \
	if573-sdio-firmware \
	${@bb.utils.contains('MACHINE_FEATURES', 'pci', 'if573-pcie-firmware', '', d)} \
	nx61x-firmware-1216-serdev \
	nx61x-firmware-1218-serdev \
	ti351-firmware \
	summit-adaptive-ww \
	summit-supplicant \
	summit-supplicant-cli \
	summit-networkmanager \
	summit-networkmanager-nmcli \
	summit-hostapd \
	${@bb.utils.contains('COMBINED_FEATURES', 'bluetooth', 'summit-adaptive-bt summit-bt-uart-scripts-60', '', d)} \
	"
