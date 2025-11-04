SUMMARY = "Summit Init Configurations"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch systemd

SSTATE_SKIP_CREATION = "1"
do_fetch[nostamp] = "1"
do_install[nostamp] = "1"

SRC_URI = " \
    file://LICENSE.ezurio \
    file://rootfs-additions-common/ \
    "

SRC_URI:append:summit-secure = " \
    file://summit-secure/rootfs-additions/ \
    "

SRC_URI:append:imx8mp-summitsom = " \
    file://imx8mp-summitsom/rootfs-additions/ \
    "

S = "${UNPACKDIR}"

FILES:${PN} += "${sbindir} ${libdir} ${nonarch_libdir} ${systemd_system_unitdir} ${sysconfdir} ${datadir} /perm /data"

RDEPENDS:${PN} = "\
    libubootenv-bin \
    util-linux-blkid \
    util-linux-lsblk \
    iptables \
    ${PREFERRED_PROVIDER_virtual/bootloader}-env \
    "

RDEPENDS:${PN}:append:summit-secure = "\
    keyutils \
    libdevmapper \
    e2fsprogs-mke2fs \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom:summit-secure = " keyctl-caam"

do_install () {
    cp -r --preserve=links,timestamps -t "${D}" "${S}"/rootfs-additions-common/*
    find "${D}" -type f -name .empty -delete
    find "${D}${libdir}/NetworkManager/system-connections" -type f -exec chmod 600 {} \;
}

do_install:prepend:summit-secure () {
    cp -r --preserve=links,timestamps -t "${D}" "${S}"/summit-secure/rootfs-additions/*
}

do_install:prepend:imx8mp-summitsom () {
    cp -r --preserve=links,timestamps -t "${D}" "${S}"/imx8mp-summitsom/rootfs-additions/*
}

SYSTEMD_SERVICE:${PN} = "mount_boot.service fw_env.service"
SYSTEMD_AUTO_ENABLE = "enable"

SYSTEMD_SERVICE:${PN}:append:summit-secure = " mount_data.service var-lib-bluetooth.mount var-log-journal.mount"
