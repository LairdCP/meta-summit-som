
SUMMARY = "Highly-optimized, pure-python HTTP server"
HOMEPAGE = "https://cheroot.cherrypy.dev"
AUTHOR = "CherryPy Team <team@cherrypy.dev>"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=c4e17b64eab9c128f786f44f0dfb570a"

SRC_URI += "\
        file://0001-disable-packages-to-save-memory.patch \
        file://0002-improve-ssl-error-handling.patch \
"

SRC_URI[md5sum] = "70247d0948899f453b50e6181cddd0d7"
SRC_URI[sha256sum] = "366adf6e7cac9555486c2d1be6297993022eff6f8c4655c1443268cca3f08e25"

PYPI_PACKAGE = "cheroot"

inherit pypi setuptools3

RDEPENDS_${PN} = "\
        python3-io \
        python3-six \
        python3-jaraco-functools \
"
