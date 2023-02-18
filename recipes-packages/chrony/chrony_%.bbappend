FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/${PN}:"

SRC_URI:append:summitsom = " file://supplemental.sources"

do_install:append:summitsom() {
        install -D -m 0700 -t ${D}${sysconfdir}/NetworkManager/dispatcher.d \
                ${S}/examples/chrony.nm-dispatcher.dhcp \
                ${S}/examples/chrony.nm-dispatcher.onoffline

	install -D -t ${D}${sysconfdir}/chrony -m 644 ${WORKDIR}/supplemental.sources
}