FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://journald.conf"

PACKAGECONFIG_remove = "networkd timesyncd"

do_install_append() {
        install -Dm 0644 ${WORKDIR}/journald.conf ${D}${sysconfdir}/systemd/journald.conf
}
