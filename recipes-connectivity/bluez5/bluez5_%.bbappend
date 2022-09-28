PACKAGECONFIG_remove = "obex-profiles deprecated"

do_install:append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/bluetooth/main.conf
}
