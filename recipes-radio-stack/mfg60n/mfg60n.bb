SUMMARY = "Laird Connectivity Wi-Fi 60 Manufacturing tools"
SECTION = "Wireless"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

include ../radio-stack-version.inc

HFP = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'eabihf', 'eabi', d)}"

LRD_URI = "https://files.devops.rfpros.com/builds/linux/mfg60n/laird/${PV}"

MFG_NAME_x86 = "mfg60n-x86-${PV}"
MFG_NAME_x86-64 = "mfg60n-x86_64-${PV}"
MFG_NAME_powerpc64 = "mfg60n-powerpc-e5500-${PV}"
MFG_NAME_arm = " mfg60n-arm-${HFP}-${PV}"
MFG_NAME_aarch64 = "mfg60n-aarch64-${PV}"

SRC_URI = "${LRD_URI}/${MFG_NAME}.tar.bz2;subdir=src"

S = "${WORKDIR}/src"
B = "${WORKDIR}/build"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES_${PN} += "${nonarch_base_libdir}/*"

INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

RDEPENDS_${PN} += " \
	libedit libnl libnl-genl libnl-route \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez5', '', d)} \
	"

do_unpack_append() {
    import tarfile, os
    mfgname = d.expand(d.getVar("MFG_NAME", True))
    s = d.expand(d.getVar("S", True))
    b = d.expand(d.getVar("B", True))
    os.chdir(s)
    os.system("./" + mfgname + ".sh tar")
    tf = tarfile.open(s + "/" + mfgname + ".tar.bz2")
    tf.extractall(b)
}

do_install() {
	install -D -m 755 ${B}/lmu ${D}${bindir}/lmu
	install -D -m 755 ${B}/lru ${D}${bindir}/lru

	if ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'true', 'false', d)}; then
		install -D -m 755 ${B}/btlru ${D}${bindir}/btlru
	fi

	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${B}/88W8997_mfg_pcie_uart_*
	install -D -m 644 -t ${D}${nonarch_base_libdir}/firmware/lrdmwl ${B}/88W8997_mfg_sdio_*
}
