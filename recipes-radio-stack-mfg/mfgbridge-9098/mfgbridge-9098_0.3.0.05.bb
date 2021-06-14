DESCRIPTION = "NXP Manufacturing Bridge"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

DEPENDS = "bluez5"

SRC_URI = "file://Bridge_${PV}-src.tgz"

S = "${WORKDIR}/src_mfgbridge/bridge"

CFLAGS += "${LDFLAGS}"

EXTRA_OEMAKE = "'CC=${CC}'"

do_install () {
    install -D -m 755 ${S}/mfgbridge ${D}${bindir}/mfgbridge
}