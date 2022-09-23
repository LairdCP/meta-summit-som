# Remove 'deprecated' from default config, but allow PACKAGECONFIG to be explicitly overridden
PACKAGECONFIG ??= "obex-profiles \
    readline \
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    a2dp-profiles \
    avrcp-profiles \
    network-profiles \
    hid-profiles \
    hog-profiles \
    tools \
    udev \
"


do_install_append() {
   install -D -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/bluetooth/main.conf
}
