SUMMARY = "Summit U-boot Environment"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch systemd

SRC_URI = " \
    file://LICENSE.ezurio \
    file://fw_env.service \
    file://gen_fw_env.sh \
    "

S = "${WORKDIR}"

SYSTEMD_SERVICE:${PN} = "fw_env.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} += "u-boot-fw-utils"

FILES:${PN} += "${systemd_system_unitdir} ${sysconfdir}"

do_install() {
    install -d "${D}${sysconfdir}"
    ln -sf /run/fw_env.config "${D}${sysconfdir}/fw_env.config"
    install -D -t "${D}${bindir}"  -m 0755 "${S}/gen_fw_env.sh"

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -t "${D}${systemd_system_unitdir}" -m 0644 "${S}/fw_env.service"
    fi
}
