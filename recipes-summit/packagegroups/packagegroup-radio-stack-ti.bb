SUMMARY = "Summit SOM Radio Stack TI"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-ti-backports \
	ti351-firmware \
	summit-supplicant-ti \
	summit-supplicant-ti-cli \
	summit-networkmanager-ti \
	summit-networkmanager-ti-nmcli \
	"
