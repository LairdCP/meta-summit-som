SUMMARY = "swupdate IPC client Python module"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "\
    file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93 \
    "

inherit setuptools3
require summit-platform-version.inc

DEPENDS += "swupdate"

SRC_URI = "git://github.com/Ezurio/lrd-userspace-examples.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_linux-lrd-userspace-examples.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/swclient"

RDEPENDS:${PN} = "python3 swupdate"
