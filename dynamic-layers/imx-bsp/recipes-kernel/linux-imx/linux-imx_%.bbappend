FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-ts3a227-jack.patch \
    file://0002-ts3a227-irq.patch \
    file://0003-gpio-add-gpio_of_helper.patch \
    file://0004-Introduce-the-BQ25790-charger-driver.patch \
    file://0006-sound_ts3a227_support.patch \
    file://0008-fsl_aud2htx-probe.patch \
    file://0010-imx-sdma.patch \
    file://0011-fsl-sai.patch \
    file://0012-fsl-easrc.patch \
    file://0013-lcdifv3.patch \
    file://0015-imx8mp-hdmi-pavi.patch \
    file://0019-dm-verity-partition-wait-fix.patch \
    file://0020-loadpin-Fixed-auto-enable-config.patch \
    "

SRC_URI:append:summitsom = " \
    file://dts \
    file://summitsom_defconfig \
    "

LOCALVERSION:summitsom = ""
SCMVERSION:summitsom = "n"

# Use our defconfig
IMX_KERNEL_CONFIG_AARCH64:summitsom = ""

NOCOPY_DEFCONFIG = "0"
NOCOPY_DEFCONFIG:summitsom = "1"
do_copy_defconfig[noexec] = "${NOCOPY_DEFCONFIG}"

# Remove kernel binary from rootfs
RRECOMMENDS:${KERNEL_PACKAGE_NAME}-base:summitsom = ""

do_patch:append:summitsom () {
    cp -a "${UNPACKDIR}/dts" "${S}/arch/arm64/boot"
}

# Build SDMA firmware into kernel
DEPENDS:append:summitsom = " firmware-imx"
do_compile:prepend:summitsom () {
    cp -a "${STAGING_LIBDIR}/firmware" "${S}/firmware"
}
