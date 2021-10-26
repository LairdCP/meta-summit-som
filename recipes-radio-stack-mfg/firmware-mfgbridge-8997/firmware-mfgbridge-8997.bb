DESCRIPTION = "NXP firmware for Manufacturing Bridge for 88W8997"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

inherit allarch

SRC_URI += " \
    file://sdio8997_uart_combo.bin \
    file://sdio8997_sdio_combo.bin \
    file://pcie8997_uart_combo.bin \
    "

S = "${WORKDIR}"

FILES_${PN} += "${nonarch_base_libdir} /home/root"

do_install () {
    install -D -m 644 ${S}/sdio8997_uart_combo.bin ${D}${nonarch_base_libdir}/firmware/nxp/sdio8997_uart_combo.bin
    install -D -m 644 ${S}/sdio8997_sdio_combo.bin ${D}${nonarch_base_libdir}/firmware/nxp/sdio8997_sdio_combo.bin
    install -D -m 644 ${S}/pcie8997_uart_combo.bin ${D}${nonarch_base_libdir}/firmware/nxp/pcie8997_uart_combo.bin
}