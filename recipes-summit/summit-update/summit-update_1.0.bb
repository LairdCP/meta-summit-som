SUMMARY = "Summit SOM Software Update Support"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

KEY_LOCATION_VALUE:${PN} ?= ""

SRC_URI = " \
    file://10-swupdate.conf \
    file://01-capability.conf \
    file://fw_update \
    "

RDEPENDS:${PN} = "\
    swupdate \
    swupdate-client \
    mmc-utils \
    curl \
"

S = "${WORKDIR}"

FILES:${PN} += "${systemd_unitdir} ${sysconfdir}"

do_install () {
    install -D -m 0755 -t ${D}${sbindir} ${S}/fw_update
    install -D -m 0644 -t ${D}${sysconfdir}/swupdate/conf.d ${S}/10-swupdate.conf

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -D -m 0644 -t ${D}${systemd_system_unitdir}/swupdate.d ${S}/01-capability.conf
    fi

    [ -z "${KEY_LOCATION_VALUE:${PN}}" ] || \
	    echo "SWUPDATE_ARGS=\"\${SWUPDATE_ARGS} -k ${KEY_LOCATION_VALUE:${PN}}\"" > ${D}${sysconfdir}/swupdate/conf.d/11-signing.conf
}
