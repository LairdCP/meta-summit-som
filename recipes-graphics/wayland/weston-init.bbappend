FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "\
    file://Ezurio_logo-White_Red.png \
"

FILES:${PN} += " \
    ${datadir}/Ezurio_logo-White_Red.png \
"

do_install:append() {
    sed -i -e "/\[shell\]/a background-image=${datadir}/Ezurio_logo-White_Red.png" "${D}${sysconfdir}/xdg/weston/weston.ini"
    sed -i -e "/\[shell\]/a background-type=centered" "${D}${sysconfdir}/xdg/weston/weston.ini"
    sed -i -e "/\[shell\]/a background-color=0xFF000000" "${D}${sysconfdir}/xdg/weston/weston.ini"

    install -d "${D}${datadir}"
    install -m 0644 "${UNPACKDIR}/Ezurio_logo-White_Red.png" "${D}${datadir}/Ezurio_logo-White_Red.png"
}
