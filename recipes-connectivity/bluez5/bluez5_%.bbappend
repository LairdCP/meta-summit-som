PACKAGECONFIG_remove = "obex-profiles"

PACKAGECONFIG_summitsom-cmd_remove = "deprecated"
PACKAGECONFIG_summitsom-wayland_remove = "deprecated"
PACKAGECONFIG_summitsom-xwayland_remove = "deprecated"

do_install_append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/bluetooth/main.conf
}
