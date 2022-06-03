SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://file-cache.conf \
    file://modem \
    file://set-radio-mode \
    file://set-m7-boot-mode \
    file://lrd.conf \
    file://lrdmwl.conf \
    file://lrd-profile.sh \
    file://overlayRoot.sh \
    file://var-volatile-log-journal.mount \
    file://perm-enable \
    file://perm-enable.service \
    file://perm.mount \
    "

S = "${WORKDIR}"

FILES_${PN} += "${systemd_unitdir}/system ${sysconfdir} /perm /data"

SYSTEMD_SERVICE_${PN} = "perm-enable.service"
SYSTEMD_AUTO_ENABLE = "enable"

SYSTEMD_SERVICE_${PN}_append_lrdsecure = " var-volatile-log-journal.mount"

do_install () {
    install -D -m 0644 ${S}/file-cache.conf ${D}${sysconfdir}/sysctl.d/file-cache.conf
    install -D -m 0644 ${S}/lrd.conf ${D}${sysconfdir}/tmpfiles.d/lrd.conf
    install -D -m 0755 ${S}/modem ${D}${bindir}/modem
    install -D -m 0755 ${S}/set-radio-mode ${D}${bindir}/set-radio-mode
    install -D -m 0755 ${S}/set-m7-boot-mode ${D}${bindir}/set-m7-boot-mode
    install -D -m 0644 ${S}/lrdmwl.conf ${D}${sysconfdir}/modprobe.d/lrdmwl.conf
    install -D -m 0755 ${S}/lrd-profile.sh ${D}${sysconfdir}/profile.d/lrd-profile.sh
    install -D -m 0755 ${S}/overlayRoot.sh ${D}${sbindir}/overlayRoot.sh
    install -d ${D}/perm
    install -D -m 0644 ${S}/perm-enable.service ${D}${systemd_unitdir}/system/perm-enable.service
    install -D -m 0755 ${S}/perm-enable ${D}${bindir}/perm-enable
    install -D -m 0644 ${S}/perm.mount ${D}${systemd_unitdir}/system/perm.mount
}

do_install_append_lrdsecure () {
    install -d ${D}/data
    install -D -m 0644 ${S}/var-volatile-log-journal.mount ${D}${systemd_unitdir}/system/var-volatile-log-journal.mount
    rm -f ${D}${sbindir}/overlayRoot.sh
}