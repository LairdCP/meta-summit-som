do_install:append() {
    install -m 755 -D "${S}/tools/uri2pem.py" "${D}${bindir}/uri2pem.py"
}

PACKAGES =+ "${PN}-uri2pem"

FILES:${PN} += " \
    ${libdir}/ossl-modules/pkcs11.so \
    "

FILES:${PN}-uri2pem = " \
    ${bindir}/uri2pem.py \
    "

RDEPENDS:${PN}-uri2pem += "${PN}"

BBCLASSEXTEND = "native nativesdk"
