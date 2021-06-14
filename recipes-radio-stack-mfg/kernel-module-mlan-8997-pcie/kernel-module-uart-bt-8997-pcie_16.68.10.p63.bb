DESCRIPTION = "NXP BT PCIE Driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module-nxp backports

SRC_URI = " \
        file://UART-BT-8997-U16-X86-16.26.10.p63-2.2-M4X14100-GPL-src.tgz \
        "

S = "${WORKDIR}/PCIE-UAPSTA-UART-BT-8997-U16-X86-W${PV}-16.26.10.p63-C4X16669_V4-MGPL/muart_src"

#MODULES_DIR = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/mlan"
MODULES_DIR = "${nonarch_base_libdir}/modules/nxp/pcie"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_install () {
        install -D -m 755 ${S}/hci_uart.ko ${D}${MODULES_DIR}/hci_uart.ko
}
