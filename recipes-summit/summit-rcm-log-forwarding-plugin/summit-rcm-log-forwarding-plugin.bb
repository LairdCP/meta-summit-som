SUMMARY = "Summit Remote Control Manager (RCM) Log Forwarding Plugin"
DESCRIPTION = "Enable log forwarding support for Summit-RCM"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc recipes-summit/summit-rcm/summit-rcm.inc

S = "${WORKDIR}/git/summit_rcm/plugins/log-forwarding"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"
RDEPENDS:${PN} += "\
    summit-rcm \
    systemd-journal-gatewayd \
    "

do_compile:prepend() {
    export SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES="\
        ${SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES} \
        summit_rcm_log_forwarding \
        summit_rcm_log_forwarding/services \
        "

    if [ "${SUMMIT_RCM_ENABLE_V2_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_log_forwarding/rest_api/v2/system \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_LEGACY_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_log_forwarding/rest_api/legacy \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_AT_INTERFACE}" = "True" ]; then
        export SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_LOG_FORWARDING_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_log_forwarding/at_interface/commands \
            "
    fi
}
