SUMMARY = "The ultra-reliable, fast ASGI+WSGI framework for building data plane APIs at scale"
HOMEPAGE = "https://falconframework.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "b5081be62f1e2d4f5140f4bad6993fd2bf8b1e503ee7261f56d324b87e41a67a"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-cython-native"

RDEPENDS:${PN} = "\
	${PYTHON_PN}-core \
	${PYTHON_PN}-io \
	${PYTHON_PN}-asyncio \
	"
