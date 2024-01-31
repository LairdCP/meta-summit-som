
SUMMARY = "Basic inter-process locks"
HOMEPAGE = "https://github.com/zopefoundation/zc.lockfile"
AUTHOR = "Zope Foundation <zope-dev@zope.org>"
LICENSE = "ZPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=78ccb3640dc841e1baecb3e27a6966b2"

SRC_URI[md5sum] = "3895445752278ddcc4578658c3c9a492"
SRC_URI[sha256sum] = "307ad78227e48be260e64896ec8886edc7eae22d8ec53e4d528ab5537a83203b"

PYPI_PACKAGE = "zc.lockfile"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"