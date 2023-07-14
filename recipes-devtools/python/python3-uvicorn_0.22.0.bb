SUMMARY = "The lightning-fast ASGI server"
HOMEPAGE = "https://www.uvicorn.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[sha256sum] = "79277ae03db57ce7d9aa0567830bbb51d7a612f54d6e1e3e92da3ef24c2c8ed8"

inherit pypi python_setuptools_build_meta

DEPENDS += "${PYTHON_PN}-hatchling-native"

RDEPENDS:${PN} += "\
	${PYTHON_PN}-core \
	${PYTHON_PN}-io \
	${PYTHON_PN}-asyncio \
	${PYTHON_PN}-logging \
	${PYTHON_PN}-click \
	${PYTHON_PN}-httptools \
	${PYTHON_PN}-websockets \
	"
