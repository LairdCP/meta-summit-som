SUMMARY = "Summit SOM 8M Plus DVK M7 Demos"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch deploy custom-fit-gen

BB_STRICT_CHECKSUM:summit-internal = "ignore"

PREMIRRORS:summit-internal = ""
MIRRORS:summit-internal = ""

LRD_URI ?= "https://github.com/LairdCP/SOM8MP-Zephyr-Release-Packages/releases/download/SUMMIT-ZEPHYR-${PV}"
LRD_URI:summit-internal = "https://files.devops.rfpros.com/builds/zephyr/som8mp/laird/${PV}"

SRC_URI = "${LRD_URI}/lrd-m7-demos-${PV}.tar.bz2"

SRC_URI[sha256sum] = "d1adfbdc94deb332dc888d3df5254100a41b052002526e3ea9ad8dec96873df2"

do_configure[noexec] = "1"

# if elf files installed, yocto checks architecture, so disable this for the firmware blobs
INSANE_SKIP:${PN} += "arch"

S = "${WORKDIR}"

FILES:${PN} += "${nonarch_base_libdir}"

do_compile () {
   fitimage_firmware "fitImageMcu.its" "lrd-m7-low-power-wakeup-demo-itcm.bin" "fitImageMcu.bin"
}

do_install () {
   install -m 0644 -D -t ${D}${nonarch_base_libdir}/firmware ${S}/lrd-m7-low-power-wakeup-demo-itcm.elf
}

do_deploy () {
   install -m 0644 -D -t ${DEPLOYDIR} ${S}/fitImageMcu.bin
}

addtask deploy after do_compile
