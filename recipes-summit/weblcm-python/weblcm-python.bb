SUMMARY = "Summit Web Configuration Utility and REST API"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3 systemd
require summit-platform-version.inc

S = "${WORKDIR}/git"

SRC_URI = "git://git@github.com/LairdCP/weblcm-python.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@git.devops.rfpros.com/cp_apps/weblcm-python.git;protocol=ssh;nobranch=1"

SRCREV = "LRD-REL-${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "python3-cython-native"

SYSTEMD_SERVICE:${PN} = "weblcm-python.service"
SYSTEMD_AUTO_ENABLE = "enable"

USERNAME:${PN} ?= "root"
PASSWORD:${PN} ?= "summit"

MANAGED_SOFTWARE_DEVICES:${PN} ?= ""
UNMANAGED_HARDWARE_DEVICES:${PN} ?= ""

ADAPTIVE_WW_CFG_FILE:${PN} ?= ""

ENABLE_UNAUTHENTICATED ?= "False"
BIND_IP ?= "::"

ENABLE_SESSIONS ?= "True"
ENABLE_CLIENT_AUTHENTICATION ?= "False"

CA_CERT_CHAIN_PATH ?= "/etc/weblcm-python/ssl/ca.crt"

ALLOW_MULTIPLE_USER_SESSIONS ?= "False"

PACKAGECONFIG[awm] = "weblcm/awm,,,python3-libconf"
PACKAGECONFIG[modem] = "weblcm/modem"
PACKAGECONFIG[bluetooth] = "weblcm/bluetooth"
PACKAGECONFIG[hid] = "weblcm/hid,,,python3-pyudev"
PACKAGECONFIG[vsp] = "weblcm/vsp"
PACKAGECONFIG[ws4] = ",,,,python3-ws4py"
PACKAGECONFIG[stunnel] = "weblcm/stunnel"
PACKAGECONFIG[iptables] = "weblcm/iptables,,,iptables"

PACKAGECONFIG ?= "awm ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth', '', d)}"

RDEPENDS:${PN} = "\
	python3 \
	python3-core \
	python3-crypt \
	python3-datetime \
	python3-io \
	python3-json \
	python3-syslog \
	python3-threading \
	python3-dbus \
	python3-pygobject \
	python3-cherrypy \
	zip \
	unzip \
	summit-update \
	swclient \
	tzdata-core \
	tzdata-posix \
	iw \
        "

do_compile:prepend() {
        export WEBLCM_PYTHON_EXTRA_PACKAGES="${PACKAGECONFIG_CONFARGS}"
}

do_install:append() {

	install -D -t ${D}${bindir}/weblcm-python.scripts -m 755 ${S}/*.sh
	install -D -t ${D}${sysconfdir}/ -m 644 ${S}/weblcm-python.ini
	install -D -t ${D}${sysconfdir}/weblcm-python/ssl -m 644 \
		${S}/ssl/server.key ${S}/ssl/server.crt ${S}/ssl/ca.crt

	sed -i -e '/^default_/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a default_password: \"${PASSWORD:${PN}}\"' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a default_username: \"${USERNAME:${PN}}\"' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^managed_software_devices/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a managed_software_devices: ${MANAGED_SOFTWARE_DEVICES:${PN}}' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^unmanaged_hardware_devices/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a unmanaged_hardware_devices: ${UNMANAGED_HARDWARE_DEVICES:${PN}}' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^awm_cfg/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a awm_cfg:${ADAPTIVE_WW_CFG_FILE:${PN}}' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^enable_allow_unauthenticated_reboot_reset/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a enable_allow_unauthenticated_reboot_reset:${ENABLE_UNAUTHENTICATED}' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^server.socket_host/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[global\]/a server.socket_host: \"${BIND_IP}\"' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^allow_multiple_user_sessions/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a allow_multiple_user_sessions: ${ALLOW_MULTIPLE_USER_SESSIONS}' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e 's,^tools.sessions.on:.*,tools.sessions.on: ${ENABLE_SESSIONS},' ${D}${sysconfdir}/weblcm-python.ini

	sed -i -e '/^enable_client_auth/d' ${D}${sysconfdir}/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a enable_client_auth: ${ENABLE_CLIENT_AUTHENTICATION}' ${D}${sysconfdir}/weblcm-python.ini

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -D -m 644 ${S}/weblcm-python.service \
			${D}${systemd_system_unitdir}/weblcm-python.service
	fi

	if [ "${ENABLE_CLIENT_AUTHENTICATION}" = "True" ]; then
		sed -i -e 's,^server.ssl_certificate_chain:.*,server.ssl_certificate_chain: \"${CA_CERT_CHAIN_PATH}\",' ${D}${sysconfdir}/weblcm-python.ini
	fi
}
