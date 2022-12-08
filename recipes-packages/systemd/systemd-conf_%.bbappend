do_install:append:summitsom() {
        rm  ${D}${systemd_unitdir}/journald.conf.d/00-${PN}.conf
}
