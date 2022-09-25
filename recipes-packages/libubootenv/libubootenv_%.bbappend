FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://0001-crc32.patch \
        file://0002-redund.patch \
        "

DEPENDS:remove = "zlib"
