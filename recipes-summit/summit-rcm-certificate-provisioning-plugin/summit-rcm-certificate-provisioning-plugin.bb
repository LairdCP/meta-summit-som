SUMMARY = "Summit Remote Control Manager (RCM) Certificate Provisioning Plugin"
DESCRIPTION = "Enable certificate provisioning support for Summit-RCM"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3
require summit-platform-version.inc recipes-summit/summit-rcm/summit-rcm.inc

S = "${WORKDIR}/git/summit_rcm/plugins/provisioning"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1"

FILESEXTRAPATHS:prepend := "${THISDIR}/../summit-rcm/summit-rcm:"

SRC_URI:append = "\
	file://ca.crt \
	file://server.crt \
	file://server.key \
	"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "\
    ${PYTHON_PN}-cython-native \
    summit-rcm \
    summit-initdata \
    "

do_compile:prepend() {
    export SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES="\
        ${SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES} \
        summit_rcm_provisioning \
        summit_rcm_provisioning/services \
        summit_rcm_provisioning/middleware \
        "

    if [ "${SUMMIT_RCM_ENABLE_V2_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_provisioning/rest_api/v2/system \
            "
    fi

    if [ "${SUMMIT_RCM_ENABLE_LEGACY_ROUTES}" = "True" ]; then
        export SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES="\
            ${SUMMIT_RCM_CERTIFICATE_PROVISIONING_PLUGIN_EXTRA_PACKAGES} \
            summit_rcm_provisioning/rest_api/legacy \
            "
    fi
}

do_install:append() {
    install -D -m 644 ${WORKDIR}/server.crt ${D}${sysconfdir}/summit-rcm/ssl/provisioning.crt
    install -D -m 644 ${WORKDIR}/server.key ${D}${sysconfdir}/summit-rcm/ssl/provisioning.key
    install -D -m 644 ${WORKDIR}/ca.crt ${D}${sysconfdir}/summit-rcm/ssl/provisioning.ca.crt
}

do_install:append:summit-secure () {
    ln -sf /data/secret/fallback_timestamp ${D}${sysconfdir}/fallback_timestamp

    mkdir -p ${D}/usr/share/factory/etc/secret/permanent/provisioning
    ln -sf /data/secret/permanent/provisioning ${D}${sysconfdir}/summit-rcm/provisioning
}

FILES:${PN} += "\
    /usr/share/factory/etc/secret/permanent/provisioning \
    "
