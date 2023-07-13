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

DEPENDS += "rsync-native"

RDEPENDS:${PN} = "util-linux-lsblk summit-fwenv"

do_install () {
    rsync -rlpDWK --no-perms --delete --exclude=.empty ${S}/rootfs-additions/ ${D}/
    chmod 600 ${D}/usr/lib/NetworkManager/system-connections/*
}

SYSTEMD_SERVICE:${PN}:summit-secure = "mount_data.service var-lib-bluetooth.mount var-log-journal.mount"
SYSTEMD_AUTO_ENABLE:summit-secure = "enable"

do_install:append:summit-secure () {
    rsync -rlpDWK --no-perms --exclude=.empty ${S}/rootfs-additions-secure/ ${D}/
    rm -f ${D}${sbindir}/overlayRoot.sh
}
