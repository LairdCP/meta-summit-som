SUMMARY = "Python minimalist web framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a5ad8f932e1fd3841133f20d3ffedda1"

SRC_URI[md5sum] = "033c58bf3da497f283b039911d1c882b"
SRC_URI[sha256sum] = "9b48cfba8a2f16d5b6419cc657e6d51db005ba35c5e3824e4728bb03bbc7ef9b"

PYPI_PACKAGE = "CherryPy"

inherit pypi setuptools3

SRC_URI += "\
    file://0001-disable-packages-to-save-memory.patch \
    file://0002-For-now-inline-the-implementation.patch \
"

DEPENDS += "\
    python3-setuptools-scm-native \
"

RDEPENDS_${PN} += " \
    python3-io \
    python3-email \
    python3-threading \
    python3-cheroot \
    python3-portend \
    python3-more-itertools \
    python3-zc-lockfile \
    "
