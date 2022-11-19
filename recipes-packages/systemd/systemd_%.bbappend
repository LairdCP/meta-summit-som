FILESEXTRAPATHS_prepend_summitsom := "${THISDIR}/files:"

SRC_URI_append_summitsom = "\
    file://journald.conf \
    file://0005-timedate-symlink.patch \
    "

PACKAGECONFIG_remove_summitsom = "\
    networkd \
    timesyncd \
    randomseed \
    resolved \
    resolv-conf \
    nss \
    nss-mymachines \
    nss-resolve \
    sysvinit \
    gshadow \
"

do_install_append_summitsom() {
        install -Dm 0644 ${WORKDIR}/journald.conf ${D}${sysconfdir}/systemd/journald.conf
        rm -f ${D}${systemd_unitdir}/system-generators/systemd-gpt-auto-generator
}
