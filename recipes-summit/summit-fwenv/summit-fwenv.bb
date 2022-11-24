SUMMARY = "Summit U-boot Environment"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://fw_env.service \
    file://gen_fw_env.sh \
    "

S = "${WORKDIR}"

SYSTEMD_SERVICE:${PN} = "fw_env.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} += "u-boot-fw-utils"

FILES:${PN} += "${systemd_system_unitdir} ${sysconfdir}"

do_install() {
    install -d ${D}${sysconfdir}/
    ln -sf /run/fw_env.config ${D}${sysconfdir}/fw_env.config
    install -D -m 0755 ${S}/gen_fw_env.sh ${D}${bindir}/gen_fw_env.sh

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 0644 ${S}/fw_env.service \
            ${D}${systemd_system_unitdir}/fw_env.service
    fi
}
