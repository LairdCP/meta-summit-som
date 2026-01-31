FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SPLASH_IMAGES:summitsom = "file://ezurio-logo-img.h;outsuffix=default"

SRC_URI:append:summitsom = " \
    file://0001-double-buffering.patch \
    file://psplash-start.service \
    file://psplash-systemd.service \
    "

SYSTEMD_SERVICE:${PN}:summitsom = "psplash-start.service psplash-systemd.service"

do_install:append:summitsom() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        rm  "${D}${systemd_system_unitdir}/psplash-start@.service" \
            "${D}${systemd_system_unitdir}/psplash-systemd.service" \
            "${D}${sysconfdir}/udev/rules.d/fb.rules"

        install -D -m 644 -t "${D}${systemd_system_unitdir}" \
            "${UNPACKDIR}/psplash-start.service" \
            "${UNPACKDIR}/psplash-systemd.service"
	fi
}
