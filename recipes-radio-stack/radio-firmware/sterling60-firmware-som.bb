SUMMARY = "Laird Connectivity Summit 60 Firmware for SOM"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require radio-firmware.inc radio-stack-som-version.inc

SRC_URI = " \
        ${LRD_URI}/laird-sterling60-firmware-sdio-sdio-${PV}.tar.bz2;name=sterling60-firmware-sdio-sdio \
        ${LRD_URI}/laird-sterling60-firmware-sdio-uart-${PV}.tar.bz2;name=sterling60-firmware-sdio-uart \
        ${LRD_URI}/laird-sterling60-firmware-pcie-uart-${PV}.tar.bz2;name=sterling60-firmware-pcie-uart \
        "

do_install() {
	install -m 0644 -D -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lib/firmware/lrdmwl/88W8997_ST_*
}
