SUMMARY = "Summit SOM Software Update Support"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch

KEY_LOCATION_VALUE ?= ""

SRC_URI = " \
    file://LICENSE.ezurio \
    file://10-swupdate.conf \
    file://01-capability.conf \
    file://fw_update \
    file://ubi_update_support.sh \
    file://emmc_update_support.sh \
    file://erase_som_nand \
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
    install -D -m 0755 -t "${D}${bindir}" "${S}/fw_update" "${S}/update_ubi_support.sh" "${S}/erase_som_nand"
    install -D -m 0644 -t "${D}${sysconfdir}/swupdate/conf.d" "${S}/10-swupdate.conf"

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -D -m 0644 -t "${D}${systemd_system_unitdir}/swupdate.d" "${S}/01-capability.conf"
    fi

    if [ -n "${KEY_LOCATION_VALUE}" ]; then
	    echo "SWUPDATE_ARGS=\"\${SWUPDATE_ARGS} -k ${KEY_LOCATION_VALUE}\"" > "${D}${sysconfdir}/swupdate/conf.d/11-signing.conf"
    fi
}
