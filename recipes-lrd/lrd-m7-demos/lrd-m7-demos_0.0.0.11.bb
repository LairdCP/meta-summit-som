# Copyright 2022 Laird Connectivity

SUMMARY = "Laird Connectivity Summit SOM 8M Plus DVK M7 Demos"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy allarch

BB_STRICT_CHECKSUM_laird-internal = "ignore"

PREMIRRORS_laird-internal = ""
MIRRORS_laird-internal = ""

LRD_URI ?= "${LRD_URI_BASE}"
LRD_URI_laird-internal = "https://files.devops.rfpros.com/builds/zephyr/som8mp/laird/${PV}"

SRC_URI = " \
    ${LRD_URI}/lrd-m7-demos-${PV}.tar.bz2;name=lrd-m7-demos \
    "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

S = "${WORKDIR}"

do_deploy () {
   # Install the demo binaries
   install -m 0644 ${S}/lrd-m7-low-power-wakeup-demo-itcm.bin ${DEPLOYDIR}/
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_SOCARCH}"
