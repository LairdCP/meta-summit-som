PACKAGECONFIG:remove = "obex-profiles"

PACKAGECONFIG:remove:summitsom-cmd: = "deprecated"
PACKAGECONFIG:remove:summitsom-wayland = "deprecated"
PACKAGECONFIG:remove:summitsom-xwayland = "deprecated"

do_install:append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/bluetooth/main.conf
}
