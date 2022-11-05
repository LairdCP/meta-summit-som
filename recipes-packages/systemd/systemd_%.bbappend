FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/files:"

SRC_URI:append:summitsom = " file://journald.conf"

PACKAGECONFIG:remove:summitsom = "\
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

do_install:append:summitsom() {
        install -Dm 0644 ${WORKDIR}/journald.conf ${D}${sysconfdir}/systemd/journald.conf
        rm -f ${D}${systemd_unitdir}/system-generators/systemd-gpt-auto-generator
}
