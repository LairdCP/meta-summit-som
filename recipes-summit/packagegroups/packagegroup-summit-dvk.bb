SUMMARY = "Summit SOM DVK"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    summit-set-mode \
    python3 \
    python3-dbus-fast \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'packagegroup-summit-radio-stack-60', '', d)} \
    qfirehose \
    "

RDEPENDS:${PN}:append:k3:summitsom = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'packagegroup-summit-radio-stack-combo', '', d)} \
    kernel-module-tac5x1x \
    kernel-module-pivariety \
    "
