LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "\
    file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93 \
    "

inherit python_setuptools_build_meta summit-platform-version

SRC_URI = "git://github.com/Ezurio/summit-rcm.git;protocol=https;${SUMMIT_PLATFORM_BRANCH}"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;${SUMMIT_PLATFORM_BRANCH}"

DEPENDS += "${PYTHON_PN}-cython-native"    

# Other options: legacy_routes, at_interface
SUMMIT_RCM_PROTOCOLS ?= "v2_routes"

PACKAGECONFIG ?= "\
    ${SUMMIT_RCM_PROTOCOLS} \
    ${@bb.utils.contains('SUMMIT_RCM_PROTOCOLS', 'v2_routes', 'login-sessions', '', d)} \
    "

PACKAGECONFIG[multiple-user-sessions] ?= ""
PACKAGECONFIG[client-authentication] ?= ""
PACKAGECONFIG[unauthenticated-reboot-reset] ?= ""
PACKAGECONFIG[login-sessions] ?= ""
PACKAGECONFIG[v2_routes] ?= ""
PACKAGECONFIG[legacy_routes] ?= ""
PACKAGECONFIG[at_interface] ?= ""
