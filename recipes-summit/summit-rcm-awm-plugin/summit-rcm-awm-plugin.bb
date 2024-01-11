SUMMARY = "Summit Remote Control Manager (RCM) Adaptive Worldwide Mode (AWM) Plugin"
DESCRIPTION = "Add AWM (Adaptive Worldwide Mode) configuration support to Summit RCM."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc

S = "${WORKDIR}/awm"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1;subpath=summit_rcm/plugins/awm"
SRC_URI:summit-internal = "git://git@github.com:rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1;subpath=summit_rcm/plugins/awm"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"
RDEPENDS:${PN} += "\
    summit-rcm \
    ${PYTHON_PN}-libconf \
    "

ADAPTIVE_WW_CFG_FILE ?= ""

do_compile:prepend() {
    export SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES="\
        ${SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES} \
        summit_rcm_awm \
        summit_rcm_awm/services \
        "

    if [ "${SUMMIT_RCM_ENABLE_V2_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_awm/rest_api/v2/network \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_LEGACY_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_awm/rest_api/legacy \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_AT_INTERFACE}" = "True" ]; then
        export SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_AWM_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_awm/at_interface/commands \
            "
    fi
}

do_install:append() {
    mkdir -p ${D}${sysconfdir}
    echo "[summit-rcm]\nawm_cfg: \"${ADAPTIVE_WW_CFG_FILE}\"" > ${D}${sysconfdir}/summit-rcm-awm.ini
}
