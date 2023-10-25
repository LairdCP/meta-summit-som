SUMMARY = "The lightning-fast ASGI server"
HOMEPAGE = "https://www.uvicorn.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[sha256sum] = "4d3cc12d7727ba72b64d12d3cc7743124074c0a69f7b201512fc50c3e3f1569a"

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
