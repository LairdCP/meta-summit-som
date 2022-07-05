
SUMMARY = "TCP port monitoring and discovery"
HOMEPAGE = "https://github.com/jaraco/portend"
AUTHOR = "Jason R. Coombs <jaraco@jaraco.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7a7126e068206290f3fe9f8d6c713ea6"

SRC_URI += "\
        file://0001-remove-tempora-module-to-save-memory.patch \
"

SRC_URI[md5sum] = "c37d676837505a6fc8dd33185c6d62a7"
SRC_URI[sha256sum] = "239e3116045ea823f6df87d6168107ad75ccc0590e37242af0cc1e98c5d224e4"

inherit pypi setuptools3

RDEPENDS_${PN} += " \
    python3-io \
    python3-datetime \
"
