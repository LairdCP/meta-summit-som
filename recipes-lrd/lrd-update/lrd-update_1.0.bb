SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

KEY_LOCATION_VALUE_${PN} ?= ""

SRC_URI = " \
    file://10-swupdate.conf \
    file://fw_update \
    "

RDEPENDS_${PN} = "\
    swupdate \
    swupdate-client \
    mmc-utils \
    curl \
"

S = "${WORKDIR}"

do_install () {
    install -D -m 0755 ${S}/fw_update ${D}${sbindir}/fw_update

    install -D -m 0644 ${S}/10-swupdate.conf ${D}${sysconfdir}/swupdate/conf.d/10-swupdate.conf

    [ -z "${KEY_LOCATION_VALUE_${PN}}" ] || \
	    echo "SWUPDATE_ARGS=\"\${SWUPDATE_ARGS} -k ${KEY_LOCATION_VALUE_${PN}}\"" > ${D}${sysconfdir}/swupdate/conf.d/11-signing.conf
}
