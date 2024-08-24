FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://1002-mtd-name.patch \
        file://1003-crc32.patch \
        file://1004-redund.patch \
        "

DEPENDS:remove = "zlib"

INSANE_SKIP:${PN} += "patch-status"
