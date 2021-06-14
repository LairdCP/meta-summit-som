SUMMARY = "Laird Connectivity 60 Radio firmware"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

include ../radio-stack-version.inc

SRC_URI = " \
	git://git@git.devops.rfpros.com/cp_linux/laird_linux-firmware.git;protocol=ssh;nobranch=1 \
	"

SRCREV = "LRD-REL-${PV}"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES_${PN} += "${nonarch_base_libdir}/*"

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

do_install() {
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lrdmwl/mfg/88W8997_mfg_pcie_*
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lrdmwl/mfg/88W8997_mfg_sdio_*
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lrdmwl/ST/88W8997_ST_pcie_*
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${S}/lrdmwl/ST/88W8997_ST_sdio_*
}