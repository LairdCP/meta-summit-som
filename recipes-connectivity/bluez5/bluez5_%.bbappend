PACKAGECONFIG_remove = "deprecated"

NOINST_TOOLS = "tools/btgatt-server"

do_install_append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/main.conf
}
