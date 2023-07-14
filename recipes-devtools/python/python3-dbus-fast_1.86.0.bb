SUMMARY = "A faster version of dbus-next"
HOMEPAGE = "https://github.com/bluetooth-devices/dbus-fast"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=729e372b5ea0168438e4fd4a00a04947"

SRC_URI[sha256sum] = "ca376a360f1bc2c3d59e9edfb5e4de5be389cca37e8c92f4539176ddf755341e"

PYPI_PACKAGE = "dbus_fast"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-poetry-core-native ${PYTHON_PN}-cython-native"

BBCLASSEXTEND = "native nativesdk"
