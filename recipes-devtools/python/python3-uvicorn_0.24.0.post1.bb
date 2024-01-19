SUMMARY = "The lightning-fast ASGI server"
HOMEPAGE = "https://www.uvicorn.org/"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5c778842f66a649636561c423c0eec2e"

SRC_URI[sha256sum] = "09c8e5a79dc466bdf28dead50093957db184de356fcdc48697bad3bde4c2588e"

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
