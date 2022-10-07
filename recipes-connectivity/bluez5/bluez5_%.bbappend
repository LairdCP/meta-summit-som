PACKAGECONFIG_remove = "obex-profiles"

PACKAGECONFIG_remove_summitsom-cmd = "deprecated"
PACKAGECONFIG_remove_summitsom-wayland = "deprecated"
PACKAGECONFIG_remove_summitsom-xwayland = "deprecated"

do_install_append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/bluetooth/main.conf
}
