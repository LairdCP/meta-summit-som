SUMMARY = "Summit SOM DVK"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    summit-set-mode \
    python3 \
    python3-dbus-fast \
    util-linux-dmesg \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom = " \
    summit-networkmanager-ppp \
    summit-networkmanager-wwan \
    qfirehose \
    "

RDEPENDS:${PN}:append:alsa:summitsom = " \
    kernel-module-tac5x1x \
    "
