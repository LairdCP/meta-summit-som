SUMMARY = "Summit Remote Control Manager (RCM) Bluetooth Plugin"
DESCRIPTION = "Bluetooth plugin for Summit RCM"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc recipes-summit/summit-rcm/summit-rcm.inc

S = "${WORKDIR}/git/summit_rcm/plugins/bluetooth"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"
RDEPENDS:${PN} += "summit-rcm"

PACKAGECONFIG[hid] = "summit_rcm_bluetooth/hid,,,${PYTHON_PN}-pyudev"
PACKAGECONFIG[vsp] = "summit_rcm_bluetooth/vsp"

PACKAGECONFIG ?= ""

do_compile:prepend() {
    export SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES="\
        ${PACKAGECONFIG_CONFARGS} \
        "

    export SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES="\
        ${SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES} \
        summit_rcm_bluetooth \
        summit_rcm_bluetooth/services \
        "

    if [ "${SUMMIT_RCM_ENABLE_V2_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_bluetooth/rest_api/v2/bluetooth \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_LEGACY_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_BLUETOOTH_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_bluetooth/rest_api/legacy \
            "
    fi
}
