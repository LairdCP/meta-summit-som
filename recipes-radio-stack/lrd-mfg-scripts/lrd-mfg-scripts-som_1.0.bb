SUMMARY = "Laird Connectivity Mfg Wi-Fi driver Loading scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = " \
    file://load-lrdmwl.sh \
    file://unload-lrdmwl.sh \
    file://bttest.sh \
    file://wifi-lrd-blacklist.conf \
    "

S = "${WORKDIR}"

FILES_${PN} += "/home/root"

do_install() {
    install -D -m 775 -t ${D}/home/root/ ${S}/*lrdmwl.sh
    install -D -m 775 ${S}/bttest.sh ${D}${bindir}/bttest.sh
    install -D -m 644 ${S}/wifi-lrd-blacklist.conf ${D}${sysconfdir}/modprobe.d/wifi-lrd-blacklist.conf
}
