SUMMARY = "Laird Connectivity Mfg Wi-Fi driver Loading scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = " \
    file://load-lrdmwl.sh \
    file://unload-lrdmwl.sh \
    "

S = "${WORKDIR}"

FILES_${PN} += "/home/root"

do_install() {
    install -D -m 755 -t ${D}/home/root/ ${S}/*.sh
}
