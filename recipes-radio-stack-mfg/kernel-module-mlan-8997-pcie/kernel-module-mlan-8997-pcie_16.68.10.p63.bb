DESCRIPTION = "NXP MLAN PCIE Driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://gpl-2.0.txt;md5=ab04ac0f249af12befccb94447c08b77"

inherit module-nxp backports

SRC_URI = " \
        file://PCIE-UAPSTA-8997-U16-X86-W${PV}-C4X16669_V4-mlan-src.tgz \
        file://PCIE-UAPSTA-8997-U16-X86-W${PV}-C4X16669_V4-MGPL-src.tgz \
        "

S = "${WORKDIR}/PCIE-UAPSTA-UART-BT-8997-U16-X86-W${PV}-16.26.10.p63-C4X16669_V4-MGPL/wlan_src"

#MODULES_DIR = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/mlan"
MODULES_DIR = "${nonarch_base_libdir}/modules/nxp/pcie"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR} CONFIG_CFG80211=m"

do_install () {
        install -D -m 755 ${S}/mlan.ko ${D}${MODULES_DIR}/mlan.ko
        install -D -m 755 ${S}/pcie8xxx.ko ${D}${MODULES_DIR}/pcie8xxx.ko

        #install -D -m 644 "${B}/${MODULES_MODULE_SYMVERS_LOCATION}"/Module.symvers ${D}${includedir}/${BPN}/Module.symvers
        # Module.symvers contains absolute path to the build directory.
        # While it doesn't actually seem to matter which path is specified,
        # clear them out to avoid confusion
        #sed -e 's:${B}/::g' -i ${D}${includedir}/${BPN}/Module.symvers
}
