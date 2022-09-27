SUMMARY = "swupdate IPC client Python module"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3

DEPENDS += "swupdate"

SRC_URI = "git://git@github.com/LairdCP/lrd-userspace-examples.git;protocol=https;branch=lrd-10.0.0.x"
SRC_URI:summit-internal = "git://git@git.devops.rfpros.com/cp_linux/lrd-userspace-examples.git;protocol=ssh;branch=lrd-10.0.0.x"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/swclient"

RDEPENDS:${PN} = "python3 swupdate"
