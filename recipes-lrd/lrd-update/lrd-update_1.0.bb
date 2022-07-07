SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://10-swupdate.conf \
    file://fw_update \
    "

RDEPENDS_${PN} = "\
	swupdate \
	swupdate-client \
"

S = "${WORKDIR}"

do_install () {
    install -D -m 0644 ${S}/10-swupdate.conf ${D}${sysconfdir}/swupdate/conf.d/10-swupdate.conf
    install -D -m 0755 ${S}/fw_update ${D}${sbindir}/fw_update
}
