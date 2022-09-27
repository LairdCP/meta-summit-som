SUMMARY = "Summit Wi-Fi Regulatory Test tools for SOM"

require mfg60n.inc radio-stack-som-version.inc

do_install_append() {
        rm -f ${D}${nonarch_base_libdir}/firmware/lrdmwl/88W8997_mfg_pcie_usb_*
        rm -f ${D}${nonarch_base_libdir}/firmware/lrdmwl/88W8997_mfg_usb_*
}