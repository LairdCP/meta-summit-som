FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SPLASH_IMAGES = "file://ezurio-logo-img.h;outsuffix=default"

SRC_URI += " \
    file://0001-double-buffering.patch \
    file://psplash-start.service \
    file://psplash-systemd.service \
    "

FILES:${PN} += "${systemd_system_unitdir}"

SYSTEMD_SERVICE:${PN} = "psplash-start.service psplash-systemd.service"

do_install:append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        rm  "${D}${systemd_system_unitdir}/psplash-start@.service" \
            "${D}${systemd_system_unitdir}/psplash-systemd.service" \
            "${D}${sysconfdir}/udev/rules.d/fb.rules"

        install -D -m 644 -t "${D}${systemd_system_unitdir}" \
            "${UNPACKDIR}/psplash-start.service" \
            "${UNPACKDIR}/psplash-systemd.service"
	fi
}
