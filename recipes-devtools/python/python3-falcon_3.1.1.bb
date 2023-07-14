SUMMARY = "The ultra-reliable, fast ASGI+WSGI framework for building data plane APIs at scale"
HOMEPAGE = "https://falconframework.org"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "5dd393dbf01cbaf99493893de4832121bd495dc49a46c571915b79c59aad7ef4"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-cython-native"

RDEPENDS:${PN} = "\
	${PYTHON_PN}-core \
	${PYTHON_PN}-io \
	${PYTHON_PN}-asyncio \
	"
