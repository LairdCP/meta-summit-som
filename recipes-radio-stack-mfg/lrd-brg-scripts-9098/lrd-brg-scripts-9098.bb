DESCRIPTION = "NXP Manufacturing Bridge support for 88W8997"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

inherit allarch

SRC_URI += " \
    file://load-brg.sh \
    file://unload-brg.sh \
    file://bridge_init.conf \
    file://wifi-blacklist.conf \
    "

S = "${WORKDIR}"

FILES_${PN} += "${nonarch_base_libdir} /home/root"

do_install () {
    install -D -m 755 ${S}/load-brg.sh ${D}/home/root/load-brg.sh
    install -D -m 755 ${S}/unload-brg.sh ${D}/home/root/unload-brg.sh
    install -D -m 644 ${S}/bridge_init.conf ${D}/home/root/bridge_init.conf
    install -D -m 644 ${S}/wifi-blacklist.conf ${D}${sysconfdir}/modprobe.d/wifi-blacklist.conf
}