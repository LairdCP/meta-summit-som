FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://1000-Make-libyaml-optional.patch \
        file://1002-mtd-name.patch \
        file://1003-crc32.patch \
        file://1004-redund.patch \
        "

DEPENDS:remove = "zlib libyaml"

EXTRA_OECMAKE += "-DNO_YML_SUPPORT=ON"
