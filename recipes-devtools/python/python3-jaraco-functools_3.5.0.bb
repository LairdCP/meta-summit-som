
SUMMARY = "Functools like those found in stdlib"
HOMEPAGE = "https://github.com/jaraco/jaraco.functools"
AUTHOR = "Jason R. Coombs <jaraco@jaraco.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7a7126e068206290f3fe9f8d6c713ea6"

SRC_URI[md5sum] = "4577c96918dffc0c61ad9ce18edfb13d"
SRC_URI[sha256sum] = "31e0e93d1027592b7b0bec6ad468db850338981ebee76ba5e212e235f4c7dda0"

PYPI_PACKAGE = "jaraco.functools"

inherit pypi setuptools3

RDEPENDS:${PN} = "python3-more-itertools"

BBCLASSEXTEND = "native nativesdk"
