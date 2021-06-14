DESCRIPTION = "NXP Manufacturing Bridge"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

inherit allarch

SRC_URI += " \
    file://FwImage/pcie9098_wlan_v1.bin \
    file://FwImage/pcieuart9098_combo_v1.bin \
    file://FwImage/uart9098_bt_v1.bin \
    "

S = "${WORKDIR}"

FILES_${PN} += "${nonarch_base_libdir} /home/root"

do_install () {
    install -D -m 755 ${S}/load.sh ${D}/home/root/load.sh
    install -D -m 755 ${S}/unload.sh ${D}/home/root/unload.sh
    install -D -m 644 ${S}/bridge_init.conf ${D}/home/root/bridge_init.conf
    install -D -m 644 ${S}/sdio8997_sdio_combo_v4_MFG_p133.bin ${D}${nonarch_base_libdir}/firmware/nxp/sdio8997_sdio_combo_v4_MFG_p133.bin
    install -D -m 644 ${S}/wifi-blacklist.conf ${D}${sysconfdir}/modprobe.d/wifi-blacklist.conf
}