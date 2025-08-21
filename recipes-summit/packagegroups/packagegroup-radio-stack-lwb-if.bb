SUMMARY = "Summit SOM Radio Stack LWB/IF Radio"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-lwb-if-backports \
	if513-sdio-div-firmware \
	if573-sdio-firmware \
	summit-supplicant \
	summit-supplicant-cli \
	summit-networkmanager \
	summit-networkmanager-nmcli \
	summit-hostapd \
	"
