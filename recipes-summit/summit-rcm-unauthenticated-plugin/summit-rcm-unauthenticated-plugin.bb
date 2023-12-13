SUMMARY = "Summit Remote Control Manager (RCM) Unauthenticated Plugin"
DESCRIPTION = "Add unauthenticated access support, for example, for factory \
reset and reboot endpoints. Due to security concerns, this \
should never be enabled unless requested by a customer, and \
preferably never enabled when Summit RCM is exposed on a \
public/external zone interface."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc recipes-summit/summit-rcm/summit-rcm.inc

S = "${WORKDIR}/git/summit_rcm/plugins/unauthenticated"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@git.devops.rfpros.com/cp_apps/summit-rcm.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"
RDEPENDS:${PN} += "summit-rcm"

do_compile:prepend() {
    export SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES="\
        ${SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES} \
        summit_rcm_unauthenticated \
        summit_rcm_unauthenticated/services \
        "

    if [ "${SUMMIT_RCM_ENABLE_V2_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_unauthenticated/rest_api/v2/system \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_LEGACY_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_UNAUTHENTICATED_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_unauthenticated/rest_api/legacy \
            "
    fi
}
