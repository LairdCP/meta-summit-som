DESCRIPTION = "QFirehose Quectel modem firmware update tool"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/NOTICE;md5=15172a07c9a201b68c4dc6611f280362"

SRC_URI = "file://QFirehose_Linux_Android_V1.4.9.zip \
           "

S = "${WORKDIR}/QFirehose_Linux_Android_V${PV}"

do_install() {
    install -D -m 755 -t ${D}${bindir} QFirehose
}
