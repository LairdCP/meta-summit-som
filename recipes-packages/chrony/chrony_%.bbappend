FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/${PN}:"

do_install:append:summitsom() {
        install -D -m 0700 -t ${D}${sysconfdir}/NetworkManager/dispatcher.d \
                ${S}/examples/chrony.nm-dispatcher.dhcp \
                ${S}/examples/chrony.nm-dispatcher.onoffline
}