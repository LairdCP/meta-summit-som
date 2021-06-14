SUMMARY = "Laird Connectivity 60 Manufacturing tools"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

include ../radio-stack-version.inc

SRC_URI = " \
	git://git@git.devops.rfpros.com/cp_linux/mfg60n.git;protocol=ssh;nobranch=1 \
	"

SRCREV = "45e9085ac9506f328a0652cec0e8a14c19eafbb9"

DEPENDS = "libnl libedit bluez5 pkgconfig-native"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "'LINKOPT=${LDFLAGS}'"

INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

do_compile () {
	oe_runmake -C lmu
	oe_runmake -C lru
	oe_runmake -C btlru
	oe_runmake -C lrt
}

do_install() {
	install -D -m 755 ${S}/lmu/bin/lmu ${D}${bindir}/lmu
	install -D -m 755 ${S}/lru/bin/lru ${D}${bindir}/lru
	install -D -m 755 ${S}/btlru/bin/btlru ${D}${bindir}/btlru
	install -D -m 755 ${S}/lrt/bin/lrt ${D}${bindir}/lrt
}
