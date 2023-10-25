SUMMARY = "A faster version of dbus-next"
HOMEPAGE = "https://github.com/bluetooth-devices/dbus-fast"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=729e372b5ea0168438e4fd4a00a04947"

SRC_URI[sha256sum] = "849478e11d251fa4ebb99ce5bfee332cb6383c63ef0bc97bae23cef4e0badf9c"

PYPI_PACKAGE = "dbus_fast"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-poetry-core-native ${PYTHON_PN}-cython-native"

BBCLASSEXTEND = "native nativesdk"
