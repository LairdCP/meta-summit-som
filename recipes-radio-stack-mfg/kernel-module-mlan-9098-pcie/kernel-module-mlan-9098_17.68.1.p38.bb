DESCRIPTION = "NXP MLAN SD Driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://gpl-2.0.txt;md5=ab04ac0f249af12befccb94447c08b77"

inherit module backports


SRC_URI = " \
        file://PCIE-WIFI-U16-X86-${PV}-MXM4X17222.P1_V0V1-MGPL-src.tgz \
        file://PCIE-WIFI-U16-X86-${PV}-MXM4X17222.P1_V0V1-mlan-src.tgz \
        "

S = "${WORKDIR}/PCIE-UAPSTA-UART-BT-9098-U16-X86-${PV}-17.26.1.p38-MXM4X17222.P1_V0V1-MGPL/wlan_src"

MODULES_DIR = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/mlan"

EXTRA_OEMAKE += "KERNELDIR='${KERNEL_SRC}' CONFIG_CFG80211=m"

do_install () {
       install -D -m 755 ${S}/mlan.ko ${D}${MODULES_DIR}/mlan.ko
       install -D -m 755 ${S}/moal.ko ${D}${MODULES_DIR}/moal.ko

        install -D -m 644 "${B}/${MODULES_MODULE_SYMVERS_LOCATION}"/Module.symvers ${D}${includedir}/${BPN}/Module.symvers
        # Module.symvers contains absolute path to the build directory.
        # While it doesn't actually seem to matter which path is specified,
        # clear them out to avoid confusion
        sed -e 's:${B}/::g' -i ${D}${includedir}/${BPN}/Module.symvers
}
