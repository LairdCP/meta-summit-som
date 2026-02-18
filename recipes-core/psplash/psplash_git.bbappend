FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/files:"

SPLASH_IMAGES:summitsom = "file://ezurio-logo-img.h;outsuffix=default"

SRC_URI:append:summitsom = " \
    file://0001-double-buffering.patch \
    file://psplash-start.service \
    file://psplash-systemd.service \
    "
    