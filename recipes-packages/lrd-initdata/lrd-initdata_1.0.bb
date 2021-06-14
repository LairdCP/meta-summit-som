SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

INITSCRIPT_NAME = "sound-setup"
SYSTEMD_SERVICE_${PN} = "sound-setup.service"
SYSTEMD_AUTO_ENABLE = "enable"

SRC_URI = " \
    file://file-cache.conf \
    file://modem \
    file://set-radio-mode \
    file://sound-setup.service \
    file://sound-setup \
     "

S = "${WORKDIR}"

FILES_${PN} += "${systemd_unitdir}/system ${sysconfdir}"

do_install() {
    install -D -m 0644 ${S}/file-cache.conf ${D}${sysconfdir}/sysctl.d/file-cache.conf
    install -D -m 0755 ${S}/modem ${D}${bindir}/modem
    install -D -m 0755 ${S}/set-radio-mode ${D}${bindir}/set-radio-mode

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 0644 ${S}/sound-setup.service \
            ${D}${systemd_unitdir}/system/sound-setup.service
    else
        install -D -m 0755 ${WORKDIR}/sound-setup.sh \
            ${D}${sysconfdir}/init.d/sound-setup
    fi
}
