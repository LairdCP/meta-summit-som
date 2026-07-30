FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/files:"

# Carbon AM62Lx 2G LPDDR4 memory configuration (matches the som-external
# Buildroot board/carbon/configs/atf-dts-62l DTS/DTSI pair).
SRC_URI:append:am62lxx:summitsom = " \
    file://am62lx-carbon-2g-lp4-50-800.dtsi \
    file://k3-am62l-carbon-2g.dts \
    "

do_compile:prepend:am62lxx:summitsom() {
    install -m 0644 -t "${S}/fdts" \
        "${UNPACKDIR}/am62lx-carbon-2g-lp4-50-800.dtsi" \
        "${UNPACKDIR}/k3-am62l-carbon-2g.dts"
}

EXTRA_OEMAKE:append:am62lxx:summitsom = "\
    DTB_FILE_NAME=k3-am62l-carbon-2g.dtb \
    "