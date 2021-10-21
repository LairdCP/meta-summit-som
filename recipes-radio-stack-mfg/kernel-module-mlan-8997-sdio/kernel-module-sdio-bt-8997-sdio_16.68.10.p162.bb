DESCRIPTION = "NXP BT SD Driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6 \
"
inherit module-nxp backports

SRC_URI = " \
        file://SD-BT-8997-U16-MMC-16.26.10.p162-C4X14114_V4-GPL-src.tgz \
        "

S = "${WORKDIR}/SD-UAPSTA-BT-8997-U16-MMC-W${PV}-16.26.10.p162-C4X16693_V4-MGPL/mbt_src"

#MODULES_DIR = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/bluetooth"
MODULES_DIR = "${nonarch_base_libdir}/modules/nxp/sdio"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_install () {
        install -D -m 755 ${S}/bt8xxx.ko ${D}${MODULES_DIR}/bt8xxx.ko
}
