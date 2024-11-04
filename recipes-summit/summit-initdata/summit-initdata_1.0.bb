SUMMARY = "Summit Init Configurations"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch systemd

SRC_URI = " \
    file://LICENSE.ezurio \
    file://rootfs-additions \
    file://rootfs-additions-secure \
    "

S = "${WORKDIR}"

FILES:${PN} += "${sbindir} ${libdir} ${systemd_system_unitdir} ${sysconfdir} /perm /data"

DEPENDS += "rsync-native"

RDEPENDS:${PN} = "util-linux-lsblk util-linux-blkid summit-fwenv iptables"

do_install () {
    rsync -rlpDWK --no-perms --delete --exclude=.empty "${S}/rootfs-additions/" "${D}/"
    chmod 600 "${D}/usr/lib/NetworkManager/system-connections/"*
    install -d "${D}${sysconfdir}/NetworkManager/certs"
}

SYSTEMD_SERVICE:${PN}:summit-secure = "mount_data.service var-lib-bluetooth.mount var-log-journal.mount"
SYSTEMD_AUTO_ENABLE:summit-secure = "enable"

do_install:append:summit-secure () {
    rsync -rlpDWK --no-perms --exclude=.empty "${S}/rootfs-additions-secure" "${D}"
    rm -f "${D}${sbindir}/overlayRoot.sh"
}
