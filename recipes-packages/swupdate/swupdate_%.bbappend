FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "\
        file://0002-fat-format.patch \
        "

SRC_URI:append:summitsom = "\
        file://0003-swupdate.patch;patchdir=../ \
        "