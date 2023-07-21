DEFCONFIG = "config_openssl"

require summit-supplicant.inc radio-stack-som-version.inc

DEPENDS += "dbus"
RRECOMMENDS:${PN} += "summit-supplicant-libs-som"

FILES:${PN} += "${datadir}/dbus-1/system-services/* ${sysconfdir}/dbus-1/system.d/*"

do_install:append() {
	install -D -t ${D}${libdir} -m 0644 wpa_supplicant/libwpa_client.so
	install -D -t ${D}${sysconfdir}/dbus-1/system.d -m 644 wpa_supplicant/dbus/dbus-wpa_supplicant.conf
	install -D -t ${D}${datadir}/dbus-1/system-services -m 644 wpa_supplicant/dbus/*.service
}
