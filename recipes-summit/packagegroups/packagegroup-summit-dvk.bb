SUMMARY = "Summit SOM DVK"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RADIO_SUPPORT ?= ""
RADIO_SUPPORT:imx8mp-summitsom = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'summit-radio', 'packagegroup-summit-radio-stack-60', '', d)}"
RADIO_SUPPORT:k3:summitsom = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'summit-radio', 'packagegroup-summit-radio-stack-combo', '', d)}"
RADIO_SUPPORT:mx9-generic-bsp:summitsom = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'summit-radio', 'packagegroup-summit-radio-stack-combo', '', d)}"

RDEPENDS:${PN} = " \
    summit-set-mode \
    python3 \
    python3-dbus-fast \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', "${RADIO_SUPPORT}", '', d)} \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom = " \
    qfirehose \
    "

RDEPENDS:${PN}:append:k3:summitsom = " \
    kernel-module-tac5x1x \
    kernel-module-pivariety \
    "

RDEPENDS:${PN}:append:mx9-generic-bsp:summitsom = " \
    kernel-module-pivariety \
    "
