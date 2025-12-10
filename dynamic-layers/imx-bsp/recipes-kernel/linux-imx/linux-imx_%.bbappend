FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:summitsom = " \
    file://0001-ts3a227-jack.patch \
    file://0006-sound_ts3a227_support.patch \
    file://0008-fsl_aud2htx-probe.patch \
    file://0010-imx-sdma.patch \
    file://0011-fsl-sai.patch \
    file://0012-fsl-easrc.patch \
    file://0013-lcdifv3.patch \
    file://0015-imx8mp-hdmi-pavi.patch \
    file://0019-dm-verity-partition-wait-fix.patch \
    file://0020-gpio-pca953x-Add-support-for-TI-TCA6418-GPIO-chip.patch \
    file://0021-sn65dsi83-nxp-bridge-compat.patch \
    file://0022-dw_mipi_dsi-imx-eprobe-defer.patch \
    file://0047-media-ov5645-Add-support-for-streams.patch \
    file://0048-media-ov5640-digient-rc-delay-compensation.patch \
    file://0049-gpio-pca953x-Add-support-for-level-triggered-interru.patch \
    file://0050-gpio-pca953x-use-regmap_update_bits-to-improve-perfo.patch \
    file://0053-loadpin-Fixed-auto-enable-config.patch\
    file://0054-media-i2c-ov5645-Report-streams-using-frame-descript.patch \
    file://0055-media-nxp-dwc-mipi-csi2.patch \
    file://dts \
    file://${KERNEL_DEFCONFIG_SUMMIT} \
    "

SRC_URI:append:imx8mp-summitsom = " \
    file://0003-gpio-add-gpio_of_helper.patch \
    file://0004-Introduce-the-BQ25790-charger-driver.patch \
    "

KERNEL_DTC_FLAGS:append:summitsom = "${@' -@' if d.getVar('KERNEL_DEVICETREE').find('.dtbo') else ''}"

KERNEL_DEFCONFIG_SUMMIT:imx8mp-summitsom ?= "summitsom_defconfig"
KERNEL_DEFCONFIG_SUMMIT:imx93-nitrogen ?= "nitrogen_imx93_defconfig"

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
DEPENDS:append:summitsom:mx8m-generic-bsp = " firmware-imx"
do_compile:prepend:summitsom:mx8m-generic-bsp () {
    cp -a "${STAGING_LIBDIR}/firmware" "${S}/firmware"
}
