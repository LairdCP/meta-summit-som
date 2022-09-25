SUMMARY = "Laird Connectivity Web Configuration Utility"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3 systemd

S = "${WORKDIR}/git"

SRC_URI = "git://git@github.com/LairdCP/weblcm-python.git;protocol=https;branch=lrd-10.0.0.x"
SRC_URI:laird-internal = "git://git@git.devops.rfpros.com/cp_apps/weblcm-python.git;protocol=ssh;branch=lrd-10.0.0.x"

SRCREV = "${AUTOREV}"

SYSTEMD_SERVICE:${PN} = "weblcm-python.service"
SYSTEMD_AUTO_ENABLE = "enable"

USERNAME:${PN} ?= "root"
PASSWORD:${PN} ?= "summit"

MANAGED_SOFTWARE_DEVICES:${PN} ?= ""
UNMANAGED_HARDWARE_DEVICES:${PN} ?= ""

ADAPTIVE_WW_CFG_FILE:${PN} ?= ""

ENABLE_UNAUTHENTICATED ?= "False"
BIND_IP ?= "::"

PACKAGECONFIG[awm] = "weblcm/awm,,,python3-libconf"
PACKAGECONFIG[modem] = "weblcm/modem"
PACKAGECONFIG[bluetooth] = "weblcm/bluetooth"
PACKAGECONFIG[hid] = "weblcm/hid,,,python3-pyudev"
PACKAGECONFIG[vsp] = "weblcm/vsp"
PACKAGECONFIG[ws4] = ",,,,python3-ws4py"

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
	lrd-update \
	swclient \
	tzdata-core \
	tzdata-posix \
        "

do_compile:prepend() {
        export WEBLCM_PYTHON_EXTRA_PACKAGES="${PACKAGECONFIG_CONFARGS}"
}

do_install:append() {
	install -D -t ${D}${localstatedir}/www/assets/fonts -m 644 ${S}/assets/fonts/*
	install -D -t ${D}${localstatedir}/www/assets/css -m 644 ${S}/assets/css/*.css
	install -D -t ${D}${localstatedir}/www/assets/img -m 644 ${S}/assets/img/*.png
	install -D -t ${D}${localstatedir}/www/assets/js -m 644 ${S}/assets/js/*.js
	install -D -t ${D}${localstatedir}/www/assets/i18n -m 644 ${S}/assets/i18n/*.json
	install -D -t ${D}${localstatedir}/www -m 644 ${S}/LICENSE

	cp -fr ${S}/plugins ${D}${localstatedir}/www/

	install -D -t ${D}${bindir}/weblcm-python.scripts -m 755 ${S}/*.sh
	install -D -t ${D}${sysconfdir}/weblcm-python -m 644 ${S}/*.ini
	install -D -t ${D}${sysconfdir}/weblcm-python/ssl -m 644 \
		${S}/ssl/server.key ${S}/ssl/server.crt ${S}/ssl/ca.crt

	sed -i -e '/^default_/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a default_password: \"${PASSWORD:${PN}}\"' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a default_username: \"${USERNAME:${PN}}\"' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	sed -i -e '/^managed_software_devices/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a managed_software_devices: ${MANAGED_SOFTWARE_DEVICES:${PN}}' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	sed -i -e '/^unmanaged_hardware_devices/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a unmanaged_hardware_devices: ${UNMANAGED_HARDWARE_DEVICES:${PN}}' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	sed -i -e '/^awm_cfg/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a awm_cfg:${ADAPTIVE_WW_CFG_FILE:${PN}}' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	sed -i -e '/^enable_allow_unauthenticated_reboot_reset/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[weblcm\]/a enable_allow_unauthenticated_reboot_reset:${ENABLE_UNAUTHENTICATED}' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	sed -i -e '/^server.socket_host/d' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini
	sed -i -e '/\[global\]/a server.socket_host: \"${BIND_IP}\"' ${D}${sysconfdir}/weblcm-python/weblcm-python.ini

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -D -m 644 ${S}/weblcm-python.service \
			${D}${systemd_unitdir}/system/weblcm-python.service
	fi
}
