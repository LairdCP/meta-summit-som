SUMMARY = "Summit SOM DVK"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    summit-set-mode \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom = " \
    kernel-module-pac193x \
    qfirehose \
    "

RDEPENDS:${PN}:append:k3:summitsom = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'packagegroup-summit-radio-stack-combo', '', d)} \
    kernel-module-tac5x1x \
    kernel-module-arducam-pivariety \
    "
