FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_install:append() {
        install -D -m 0700 ${S}/examples/chrony.nm-dispatcher.dhcp \
                ${D}${sysconfdir}/NetworkManager/dispatcher.d/chrony.nm-dispatcher.dhcp
}