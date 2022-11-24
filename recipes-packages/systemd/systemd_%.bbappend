FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/files:"

SRC_URI:append:summitsom = "\
    file://journald.conf \
    file://0005-timedate-symlink.patch \
    "

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

do_install:append:summitsom:summit-secure() {
        rm -f ${D}${sysconfdir}/systemd/journald.conf
}