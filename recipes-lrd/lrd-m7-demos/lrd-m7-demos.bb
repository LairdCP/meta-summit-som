# Copyright 2022 Laird Connectivity

SUMMARY = "Laird Connectivity Summit SOM 8M Plus DVK M7 Demos"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy allarch

SRC_URI = " \
    file://lrd-m7-low-power-wakeup-demo-itcm.bin \
    "

S = "${WORKDIR}"

do_deploy () {
   # Install the demo binaries
   install -m 0644 ${S}/*.bin ${DEPLOYDIR}/
}

addtask deploy after do_install

PACKAGE_ARCH = "${MACHINE_SOCARCH}"
