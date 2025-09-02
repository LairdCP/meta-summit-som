SUMMARY = "Summit SOM Diagnostic and debugging tools"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    summit-rcm \
    summit-rcm-chrony-plugin \
    summit-rcm-awm-plugin \
    ${@bb.utils.contains('COMBINED_FEATURES', 'bluetooth', 'summit-rcm-bluetooth-plugin', '', d)} \
    "
