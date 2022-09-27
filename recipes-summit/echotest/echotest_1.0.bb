SUMMARY = "Serial Echo Test"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3

S = "${WORKDIR}"

SRC_URI = " \
        file://echotest.py \
        file://setup.py \
        "

RDEPENDS:${PN} = "\
        python3 \
        python3-core \
        python3-io \
        python3-pyserial \
        "