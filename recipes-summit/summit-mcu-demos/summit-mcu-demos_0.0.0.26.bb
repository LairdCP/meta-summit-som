SUMMARY = "Summit SOM MCU Demos"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch deploy custom-fit-gen

BB_STRICT_CHECKSUM:summit-internal = "ignore"

PREMIRRORS:summit-internal = ""
MIRRORS:summit-internal = ""

LRD_URI ?= "https://github.com/LairdCP/Summit-SOM-Zephyr-Release-Packages/releases/download/SUMMIT-ZEPHYR-${PV}"
LRD_URI:summit-internal = "https://files.devops.rfpros.com/builds/zephyr/summitsom/laird/${PV}"

SRC_URI = "${LRD_URI}/summit-mcu-demos-${PV}.tar.bz2"

SRC_URI[sha256sum] = "c8443b9f1b60bb58a35afc546df3b650c74690f8fbe59005e5eaf7f134bddc81"

do_configure[noexec] = "1"

# if elf files installed, yocto checks architecture, so disable this for the firmware blobs
INSANE_SKIP:${PN} += "arch"

S = "${WORKDIR}"

FILES:${PN} += "${nonarch_base_libdir}"

MCU_BIN:imx8mp-summitsom = "summit-som8mplus-dvk-mcu-low-power-wakeup-demo-itcm.bin"
MCU_ELF:imx8mp-summitsom = "summit-som8mplus-dvk-mcu-low-power-wakeup-demo-itcm.elf"

do_compile () {
   fitimage_firmware "fitImageMcu.its" "${MCU_BIN}" "fitImageMcu.bin"
}

do_install () {
   install -m 0644 -D -t ${D}${nonarch_base_libdir}/firmware ${S}/${MCU_ELF}
}

do_deploy () {
   install -m 0644 -D -t ${DEPLOYDIR} ${S}/fitImageMcu.bin
}

addtask deploy after do_compile

COMPATIBLE_MACHINE = "imx8mp-summitsom"
