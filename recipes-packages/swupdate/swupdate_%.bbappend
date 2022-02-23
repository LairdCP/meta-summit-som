FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SYSTEMD_AUTO_ENABLE = "disable"

SRC_URI += "\
        file://0001-rawfile-error.patch \
        file://0002-fat-format.patch \
        "
