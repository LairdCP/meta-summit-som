SUMMARY = "The lightning-fast ASGI server"
HOMEPAGE = "https://www.uvicorn.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[sha256sum] = "4b15decdda1e72be08209e860a1e10e92439ad5b97cf44cc945fcbee66fc5788"

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
