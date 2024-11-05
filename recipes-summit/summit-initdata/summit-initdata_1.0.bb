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

RDEPENDS:${PN} = "util-linux-lsblk summit-fwenv iptables"

do_install () {
    cp -dR --preserve=mode "${S}/rootfs-additions"/* "${D}"
    find "${D}" -type f -name .empty -delete
    chmod 600 "${D}/usr/lib/NetworkManager/system-connections/"*
}

SYSTEMD_SERVICE:${PN}:summit-secure = "mount_data.service var-lib-bluetooth.mount var-log-journal.mount"
SYSTEMD_AUTO_ENABLE:summit-secure = "enable"

do_install:append:summit-secure () {
    cp -dR --preserve=mode "${S}/rootfs-additions-secure"/* "${D}"
    find "${D}" -type f -name .empty -delete
    rm -f "${D}${sbindir}/overlayRoot.sh"
}
