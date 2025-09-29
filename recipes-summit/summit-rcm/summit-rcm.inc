LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "\
    file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93 \
    "

inherit python_setuptools_build_meta summit-platform-version

SRC_URI = "${SUMMIT_EXTERNAL_GIT_URI}/summit-rcm.git;${SUMMIT_EXTERNAL_GIT_SUFFIX}"
SRC_URI:summit-internal = "${SUMMIT_INTERNAL_GIT_URI}/cp_apps-summit-rcm.git;${SUMMIT_INTERNAL_GIT_SUFFIX}"

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
