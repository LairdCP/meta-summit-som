FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/files:"

SPLASH_IMAGES:summitsom = "file://ezurio-logo-img.h;outsuffix=default"

SRC_URI:append:summitsom = " \
    file://0001-double-buffering.patch \
    file://0002-psplash-custom-color.patch \
    file://0003-psplash-18bpp.patch \
    file://psplash-start.service \
    "

SYSTEMD_SERVICE:${PN}:summitsom = "psplash-start.service psplash-systemd.service"

do_install:append:summitsom() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        rm  "${D}${systemd_system_unitdir}/psplash-start@.service" \
            "${D}${sysconfdir}/udev/rules.d/fb.rules"

        rm -rf "${D}${sysconfdir}/udev"

        install -D -m 644 -t "${D}${systemd_system_unitdir}" \
            "${UNPACKDIR}"/psplash-start.service
    fi
}
