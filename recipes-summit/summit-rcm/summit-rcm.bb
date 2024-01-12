SUMMARY = "Summit Remote Control Manager (RCM)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3 systemd
require summit-platform-version.inc

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/LairdCP/summit-rcm.git;protocol=https;nobranch=1"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_apps-summit-rcm.git;protocol=ssh;nobranch=1"

SRC_URI:append = "\
	file://ca.crt \
	file://server.crt \
	file://server.key \
	"

SRCREV = "${SUMMIT_PLATFORM_VERSION}"
PV = "${SUMMIT_PLATFORM_VERSION}+git${SRCPV}"

DEPENDS += "${PYTHON_PN}-cython-native"

SYSTEMD_SERVICE:${PN} = "summit-rcm.service"
SYSTEMD_AUTO_ENABLE = "enable"

USERNAME ?= "root"
PASSWORD ?= "summit"

MANAGED_SOFTWARE_DEVICES ?= ""
UNMANAGED_HARDWARE_DEVICES ?= ""

ADAPTIVE_WW_CFG_FILE ?= ""

CA_CERT_CHAIN_PATH ?= "/etc/summit-rcm/ssl/ca.crt"

SUMMIT_RCM_SERIAL_PORT ?= "/dev/ttymxc0"
SUMMIT_RCM_BAUD_RATE ?= "3000000"

PACKAGECONFIG[awm] = "summit_rcm/awm,,,${PYTHON_PN}-libconf"
PACKAGECONFIG[modem] = ""
PACKAGECONFIG[bluetooth] = "\
	${@'summit_rcm/rest_api/v2/bluetooth' if 'v2' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	${@'summit_rcm/bluetooth' if 'legacy' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	${@'summit_rcm/hid' if 'hid' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	${@'summit_rcm/vsp' if 'vsp' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	"
PACKAGECONFIG[hid] = ",,,${PYTHON_PN}-pyudev"
PACKAGECONFIG[vsp] = ""
PACKAGECONFIG[radio-siso-mode] = "summit_rcm/radio_siso_mode"
PACKAGECONFIG[stunnel] = "summit_rcm/stunnel,,,stunnel"
PACKAGECONFIG[iptables] = "summit_rcm/iptables,,,iptables"
PACKAGECONFIG[chrony] = "summit_rcm/chrony,,,chrony"
PACKAGECONFIG[at] = "\
	summit_rcm/at_interface \
	summit_rcm/at_interface/commands \
	summit_rcm/at_interface/services \
	${@'summit_rcm/log_forwarding/at_interface/commands' if 'log-forwarding' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	, \
	, \
	, \
	${PYTHON_PN}-pyserial-asyncio ${PYTHON_PN}-transitions"
PACKAGECONFIG[v2] = "\
	summit_rcm/rest_api/v2/system \
	summit_rcm/rest_api/v2/network \
	summit_rcm/rest_api/services \
	${@'summit_rcm/rest_api/v2/login' if 'v2' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	${@'summit_rcm/log_forwarding/rest_api/v2/system' if 'log-forwarding' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	, \
	, \
	, \
	${PYTHON_PN}-uvicorn ${PYTHON_PN}-falcon"
PACKAGECONFIG[legacy] = "\
	summit_rcm/rest_api/legacy \
	summit_rcm/rest_api/services \
	${@'summit_rcm/modem' if 'modem' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	${@'summit_rcm/log_forwarding/rest_api/legacy' if 'log-forwarding' in d.getVar('PACKAGECONFIG').split(' ') else ''} \
	, \
	, \
	, \
	${PYTHON_PN}-uvicorn ${PYTHON_PN}-falcon"
PACKAGECONFIG[login-sessions] = ""
PACKAGECONFIG[multiple-user-sessions] = ""
PACKAGECONFIG[unauthenticated-reboot-reset] = ""
PACKAGECONFIG[client-authentication] = ""
PACKAGECONFIG[log-forwarding] = "summit_rcm/log_forwarding/services,,,systemd-journal-gatewayd"

PACKAGECONFIG ?= "v2 awm chrony login-sessions ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth', '', d)}"

RDEPENDS:${PN} += "\
	${PYTHON_PN} \
	${PYTHON_PN}-core \
	${PYTHON_PN}-crypt \
	${PYTHON_PN}-datetime \
	${PYTHON_PN}-io \
	${PYTHON_PN}-asyncio \
	${PYTHON_PN}-json \
	${PYTHON_PN}-syslog \
	${PYTHON_PN}-threading \
	${PYTHON_PN}-aiofiles \
	${PYTHON_PN}-dbus-fast \
	summit-update \
	swclient \
	tzdata-core \
	tzdata-posix \
	iw \
    "

do_compile:prepend() {
	export SUMMIT_RCM_EXTRA_PACKAGES="summit_rcm/services ${PACKAGECONFIG_CONFARGS}"
}

do_install:append() {
	install -D -m 644 -t ${D}${sysconfdir}/ ${S}/summit-rcm.ini
	install -D -m 644 -t ${D}${sysconfdir}/summit-rcm/ssl \
		${WORKDIR}/server.key ${WORKDIR}/server.crt ${WORKDIR}/ca.crt

	sed -i -e '/^default_/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a default_password: \"${PASSWORD}\"' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a default_username: \"${USERNAME}\"' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^managed_software_devices/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a managed_software_devices: ${MANAGED_SOFTWARE_DEVICES}' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^unmanaged_hardware_devices/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a unmanaged_hardware_devices: ${UNMANAGED_HARDWARE_DEVICES}' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^awm_cfg/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a awm_cfg:${ADAPTIVE_WW_CFG_FILE}' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^enable_allow_unauthenticated_reboot_reset/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a enable_allow_unauthenticated_reboot_reset: ${@bb.utils.contains('PACKAGECONFIG','unauthenticated-reboot-reset','True','False',d)}' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^allow_multiple_user_sessions/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a allow_multiple_user_sessions: ${@bb.utils.contains('PACKAGECONFIG','multiple-user-sessions','True','False',d)}' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e 's,^tools.sessions.on:.*,tools.sessions.on: ${@bb.utils.contains('PACKAGECONFIG','login-sessions','True','False',d)},' ${D}${sysconfdir}/summit-rcm.ini

	sed -i -e '/^enable_client_auth/d' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a enable_client_auth: ${@bb.utils.contains('PACKAGECONFIG','client-authentication','True','False',d)}' ${D}${sysconfdir}/summit-rcm.ini

	if ${@bb.utils.contains('PACKAGECONFIG','client-authentication','true','false',d)}; then
		sed -i -e 's,^server.ssl_certificate_chain:.*,server.ssl_certificate_chain: \"${CA_CERT_CHAIN_PATH}\",' ${D}${sysconfdir}/summit-rcm.ini
	fi

	sed -i -e '/\[summit-rcm\]/a serial_port: \"${SUMMIT_RCM_SERIAL_PORT}\"' ${D}${sysconfdir}/summit-rcm.ini
	sed -i -e '/\[summit-rcm\]/a baud_rate: ${SUMMIT_RCM_BAUD_RATE}' ${D}${sysconfdir}/summit-rcm.ini

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -D -m 644 -t ${D}${systemd_system_unitdir} ${S}/summit-rcm.service
	fi
}
