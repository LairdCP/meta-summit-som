FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "\
        file://0001-rawfile-error.patch \
        file://0002-fat-format.patch \
        "

SYSTEMD_SERVICE:${PN}:summitsom = "swupdate.socket"