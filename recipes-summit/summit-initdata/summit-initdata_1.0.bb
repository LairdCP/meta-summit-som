SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://rootfs-additions \
    file://rootfs-additions-secure \
    "

S = "${WORKDIR}"

FILES_${PN} += "${sbindir} ${libdir} ${systemd_system_unitdir} ${sysconfdir} /perm /data"

SYSTEMD_SERVICE_${PN} = "perm-enable.service"
SYSTEMD_AUTO_ENABLE = "enable"

DEPENDS += "rsync-native"

RDEPENDS_${PN} = "summit-fwenv"

do_install () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions/ ${D}/
}

SYSTEMD_SERVICE_${PN}_append_summit-secure = " var-volatile-log-journal.mount mount_data.service"

do_install_append_summit-secure () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions-secure/ ${D}/
    rm -f ${D}${sbindir}/overlayRoot.sh
}