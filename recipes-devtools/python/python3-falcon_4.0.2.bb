SUMMARY = "The ultra-reliable, fast ASGI+WSGI framework for building data plane APIs at scale"
HOMEPAGE = "https://falconframework.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "58f4b9c9da4c9b1e2c9f396ad7ef897701b3c7c7c87227f0bd1aee40c7fbc525"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-cython-native"

RDEPENDS:${PN} = "\
	${PYTHON_PN}-core \
	${PYTHON_PN}-io \
	${PYTHON_PN}-asyncio \
	"
