SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://rootfs-additions \
    file://var-volatile-log-journal.mount \
    "

S = "${WORKDIR}"

FILES:${PN} += "${libdir} ${systemd_system_unitdir} ${sysconfdir} /perm /data"

SYSTEMD_SERVICE:${PN} = "perm-enable.service"
SYSTEMD_AUTO_ENABLE = "enable"

SYSTEMD_SERVICE:${PN}:append:lrdsecure = " var-volatile-log-journal.mount"

RDEPENDS:${PN} = "summit-fwenv"

do_install () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions/ ${D}/
}

do_install:append:lrdsecure () {
    install -d ${D}/data
    install -D -m 0644 ${S}/var-volatile-log-journal.mount ${D}${systemd_unitdir}/system/var-volatile-log-journal.mount
    rm -f ${D}${sbindir}/overlayRoot.sh
}