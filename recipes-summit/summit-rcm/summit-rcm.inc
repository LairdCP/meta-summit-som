LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "\
    file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93 \
    "

inherit setuptools3
require summit-platform-version.inc

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"    

# Other options: legacy_routes, at_interface
SUMMIT_RCM_PROTOCOLS ?= "v2_routes"
