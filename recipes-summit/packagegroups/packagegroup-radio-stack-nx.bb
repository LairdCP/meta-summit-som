SUMMARY = "Summit SOM Radio Stack NX"
SECnxON = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
	kernel-module-nx-backports \
	nx61x-firmware-1216-serdev \
	nx61x-firmware-1218-serdev \
	summit-supplicant \
	summit-supplicant-cli \
	summit-networkmanager \
	summit-networkmanager-nmcli \
	"
