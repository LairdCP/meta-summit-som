SUMMARY = "Laird Connectivity Summit SOM 8M Plus DVK M7 Demos"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch deploy

BB_STRICT_CHECKSUM_laird-internal = "ignore"

PREMIRRORS_laird-internal = ""
MIRRORS_laird-internal = ""

LRD_URI ?= "https://github.com/LairdCP/SOM8MP-Zephyr-Release-Packages/releases/download/SUMMIT-ZEPHYR-${PV}"
LRD_URI_laird-internal = "https://files.devops.rfpros.com/builds/zephyr/som8mp/laird/${PV}"

SRC_URI = "${LRD_URI}/lrd-m7-demos-${PV}.tar.bz2"

SRC_URI[sha256sum] = "bec121eae520c3196745972a55904372cde349525d78ff95bf7772e9a37f491b"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# if elf files installed, yocto checks architecture, so disable this for the firmware blobs
INSANE_SKIP_${PN} += "arch"

S = "${WORKDIR}"

FILES_${PN} += "${nonarch_base_libdir}"

do_install() {
   install -m 0644 -D -t ${D}${nonarch_base_libdir}/firmware ${S}/lrd-m7-low-power-wakeup-demo-itcm.elf
}

do_deploy () {
   install -m 0644 -D -t ${DEPLOYDIR} ${S}/lrd-m7-low-power-wakeup-demo-itcm.bin
}

addtask deploy after do_install
