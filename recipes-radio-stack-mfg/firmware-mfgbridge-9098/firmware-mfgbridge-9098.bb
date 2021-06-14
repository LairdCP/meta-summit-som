DESCRIPTION = "NXP Manufacturing Bridge"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

inherit allarch

SRC_URI += " \
    file://load.sh \
    file://unload.sh \
    file://bridge_init.conf \
    file://wifi-blacklist.conf \
    file://pcie9098_wlan.bin \
    file://pcieuart9098_combo.bin \
    file://sdio9098_wlan.bin \
    file://sdiosdio9098_combo.bin \
    "

S = "${WORKDIR}"

FILES_${PN} += "${nonarch_base_libdir} /home/root"

do_install () {
    install -D -m 755 ${S}/load.sh ${D}/home/root/load.sh
    install -D -m 755 ${S}/unload.sh ${D}/home/root/unload.sh
    install -D -m 644 ${S}/bridge_init.conf ${D}/home/root/bridge_init.conf
    install -D -m 644 ${S}/wifi-blacklist.conf ${D}/home/root/wifi-blacklist.conf
    install -D -m 644 ${S}/pcie9098_wlan.bin ${D}${nonarch_base_libdir}/firmware/nxp/pcie9098_wlan.bin
    install -D -m 644 ${S}/pcieuart9098_combo.bin ${D}${nonarch_base_libdir}/firmware/nxp/pcieuart9098_combo.bin
    install -D -m 644 ${S}/sdio9098_wlan.bin ${D}${nonarch_base_libdir}/firmware/nxp/sdio9098_wlan.bin
    install -D -m 644 ${S}/sdiosdio9098_combo.bin ${D}${nonarch_base_libdir}/firmware/nxp/sdiosdio9098_combo.bin
}