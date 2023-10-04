SUMMARY = "Summit Regulatory Test Wi-Fi driver Loading scripts"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = " \
    file://load-lrdmwl.sh \
    file://unload-lrdmwl.sh \
    file://bttest.sh \
    file://wifi-summit-blacklist.conf \
    file://lrdmwl_2040.conf \
    "

RDEPENDS:${PN} = "\
	libubootenv \
"

S = "${WORKDIR}"

FILES:${PN} += "/home/root"

do_install() {
    install -D -m 0775 -t ${D}/home/root ${S}/*lrdmwl.sh
    install -D -m 0775 -t ${D}${bindir} ${S}/bttest.sh
    install -D -m 0644 -t ${D}${sysconfdir}/modprobe.d ${S}/wifi-summit-blacklist.conf
    install -D -m 0644 -t ${D}${sysconfdir}/modprobe.d ${S}/lrdmwl_2040.conf
}
