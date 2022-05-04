SUMMARY = "Python minimalist web framework"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a8cbc5da4e6892b15a972a0b18622b2b"

SRC_URI[md5sum] = "01dec1a7164faf2406a3a03bf56aef80"
SRC_URI[sha256sum] = "f33e87286e7b3e309e04e7225d8e49382d9d7773e6092241d7f613893c563495"

PYPI_PACKAGE = "CherryPy"

inherit pypi setuptools3

RDEPENDS_${PN} += " \
    python3-io \
    python3-email \
    python3-threading \
    python3-cheroot \
    python3-portend \
    python3-more_itertools \
    python3-zc-lockfile \
    "
