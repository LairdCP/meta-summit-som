SUMMARY = "swupdate IPC client Python module"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc

DEPENDS += "swupdate"

SRC_URI = "git://github.com/LairdCP/lrd-userspace-examples.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@git.devops.rfpros.com/cp_linux/lrd-userspace-examples.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/swclient"

RDEPENDS:${PN} = "python3 swupdate"
