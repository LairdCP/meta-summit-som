SUMMARY = "Laird Connectivity 60 Radio firmware"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

include ../../recipes-radio-stack/radio-stack-version.inc

SUMMARY = "Laird Connectivity Sterling 60 Firmware"

LRD_URI = "https://files.devops.rfpros.com/builds/linux/firmware/${PV}"

SRC_URI = " \
        ${LRD_URI}/laird-sterling60-firmware-sdio-sdio-${PV}.tar.bz2 \
        ${LRD_URI}/laird-sterling60-firmware-sdio-uart-${PV}.tar.bz2 \
        ${LRD_URI}/laird-sterling60-firmware-pcie-uart-${PV}.tar.bz2 \
        "

S = "${WORKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES_${PN} += "${nonarch_base_libdir}/*"

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

do_install() {
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lib/firmware/lrdmwl/88W8997_ST_pcie_*
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lib/firmware/lrdmwl/88W8997_ST_sdio_*
}