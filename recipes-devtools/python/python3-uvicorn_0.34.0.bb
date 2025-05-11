SUMMARY = "The lightning-fast ASGI server"
HOMEPAGE = "https://www.uvicorn.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[sha256sum] = "404051050cd7e905de2c9a7e61790943440b3416f49cb409f965d9dcd0fa73e9"

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

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
	file://1001-implement-asgiref-tls-extension.patch \
	"
