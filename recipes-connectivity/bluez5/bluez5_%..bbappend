PACKAGECONFIG_remove = "deprecated"

do_install_append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/main.conf
}
