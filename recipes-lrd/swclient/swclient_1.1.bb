SUMMARY = "swupdate IPC client Python module"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3

DEPENDS += "swupdate"

S = "${WORKDIR}"

SRC_URI = "file://swclient.c file://setup.py"

RDEPENDS_${PN} = "python3 swupdate"
