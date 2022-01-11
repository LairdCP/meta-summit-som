SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = " \
    file://file-cache.conf \
    file://fw_env.config \
    file://modem \
    file://set-radio-mode \
    file://lrd.conf \
    file://lrdmwl.conf \
     "

S = "${WORKDIR}"

FILES_${PN} += "${systemd_unitdir}/system ${sysconfdir}"

do_install() {
    install -D -m 0644 ${S}/file-cache.conf ${D}${sysconfdir}/sysctl.d/file-cache.conf
    install -D -m 0644 ${S}/fw_env.config ${D}${sysconfdir}/fw_env.config
    install -D -m 0644 ${S}/lrd.conf ${D}${sysconfdir}/tmpfiles.d/lrd.conf
    install -D -m 0755 ${S}/modem ${D}${bindir}/modem
    install -D -m 0755 ${S}/set-radio-mode ${D}${bindir}/set-radio-mode
    install -D -m 0644 ${S}/lrdmwl.conf ${D}${sysconfdir}/modprobe.d/lrdmwl.conf
}
