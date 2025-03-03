SUMMARY = "Summit SOM Radio Stack"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-lwb-if-backports \
	if573-sdio-firmware \
	summit-supplicant-lwb-if \
	summit-supplicant-lwb-if-cli \
	summit-networkmanager-lwb-if \
	summit-networkmanager-lwb-if-nmcli \
	"
