SUMMARY = "Laird Connectivity Init Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://rootfs-additions \
    file://rootfs-additions-secure \
    "

S = "${WORKDIR}"

FILES:${PN} += "${sbindir} ${libdir} ${systemd_system_unitdir} ${sysconfdir} /perm /data"

SYSTEMD_SERVICE:${PN} = "perm-enable.service"
SYSTEMD_AUTO_ENABLE = "enable"

DEPENDS += "rsync-native"

RDEPENDS:${PN} = "summit-fwenv"

do_install () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions/ ${D}/
}

SYSTEMD_SERVICE:${PN}:append:summit-secure = " var-volatile-log-journal.mount mount_data.service"

do_install:append:summit-secure () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions-secure/ ${D}/
    rm -f ${D}${sbindir}/overlayRoot.sh
}