SUMMARY = "Summit Init Configurations"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch systemd

SSTATE_SKIP_CREATION = "1"
do_install[nostamp] = "1"

SRC_URI = " \
    file://LICENSE.ezurio \
    file://rootfs-additions-common/ \
    file://rootfs-additions/ \
    "

S = "${WORKDIR}"

FILES:${PN} += "${sbindir} ${libdir} ${systemd_system_unitdir} ${sysconfdir} ${datadir} /perm /data"

RDEPENDS:${PN} = "\
    libubootenv-bin \
    util-linux-blkid \
    util-linux-lsblk \
    iptables \
    ${PREFERRED_PROVIDER_virtual/bootloader}-env \
    "

RDEPENDS:${PN}:append:summit-secure = "\
    keyutils \
    lvm2 \
    e2fsprogs-mke2fs \
    "

RDEPENDS:${PN}:append:imx-generic-bsp:summit-secure = " keyctl-caam"

do_install () {
    cp -r --preserve=links,timestamps -t "${D}" "${S}"/rootfs-additions-common/*
    if ls "${S}"/rootfs-additions/* >/dev/null 2>&1; then
        cp -r --preserve=links,timestamps -t "${D}" "${S}"/rootfs-additions/*
    fi
    find "${D}" -type f -name .empty -delete
    find "${D}${libdir}/NetworkManager/system-connections" -type f -exec chmod 600 {} \;
}

SYSTEMD_SERVICE:${PN} = "mount_boot.service fw_env.service"
SYSTEMD_AUTO_ENABLE = "enable"

SYSTEMD_SERVICE:${PN}:append:summit-secure = " mount_data.service var-lib-bluetooth.mount var-log-journal.mount"
