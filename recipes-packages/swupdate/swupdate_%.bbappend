FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://0001-rawfile-error.patch \
        file://0002-fat-format.patch \
        "
