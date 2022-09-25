FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://journald.conf"

PACKAGECONFIG:remove = "\
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

do_install:append() {
        install -Dm 0644 ${WORKDIR}/journald.conf ${D}${sysconfdir}/systemd/journald.conf
}
