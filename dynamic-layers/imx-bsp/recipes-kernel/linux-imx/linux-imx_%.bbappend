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
    file://0054-rtc-rv3028-fix-eeprom-device-tree-support.patch \
    file://0055-rtc-rv3028-fix-name-collisions.patch \
    file://0056-media-i2c-ov5645-Report-streams-using-frame-descript.patch \
    file://0057-media-nxp-dwc-mipi-csi2.patch \
    file://0058-sn65dsi83-ignore-pll-lock-failure.patch \
    file://0059-imx-mipi-csis-enable-camera-link.patch \
    file://0060-sec_mipi_dsim-imx-probe.patch \
    file://0061-mxc-viv-disable-vg-fix.patch \
    file://0062-gpio-Add-GPIO-fanout-driver.patch \
    file://0063-phy-freescale-imx8mq-usb-set-vbus-depending-on-mode.patch \
    file://0064-media-imx-isi-cap-add-pivariety-workaround.patch \
    file://0065-media-imx-isi-cap-fix-subdev-stream-default-value.patch \
    file://0068-pinctrl-mcp23s08-add-wakeup-support.patch \
    file://dts \
    ${KERNEL_CONFIG_SUMMIT} \
    "

SRC_URI:append:imx8mp-summitsom = " \
    file://0004-Introduce-the-BQ25790-charger-driver.patch \
    "

KERNEL_DTC_FLAGS:append:summitsom = " ${@' -@' if d.getVar('KERNEL_DEVICETREE').find('.dtbo') else ''}"

KERNEL_CONFIG_SUMMIT:mx8mm-generic-bsp = "file://nitrogen_imx8mm_defconfig"
KERNEL_CONFIG_SUMMIT:mx8mp-generic-bsp = "file://nitrogen_imx8mp_defconfig"
KERNEL_CONFIG_SUMMIT:mx93-generic-bsp = "file://nitrogen_imx93_defconfig"
KERNEL_CONFIG_SUMMIT:mx95-generic-bsp = "file://nitrogen_imx95_defconfig"
KERNEL_CONFIG_SUMMIT:imx8mp-summitsom = "file://summitsom_defconfig"
KERNEL_CONFIG_SUMMIT ?= ""

LOCALVERSION:summitsom = ""
SCMVERSION:summitsom = "n"

# Use our defconfig
IMX_KERNEL_CONFIG_AARCH64:summitsom = ""

# Remove kernel binary from rootfs
RRECOMMENDS:${KERNEL_PACKAGE_NAME}-base:summitsom = ""

python __anonymous() {
    if 'summitsom' in d.getVar('OVERRIDES', True).split(':'):
        d.setVarFlag('do_copy_defconfig', 'noexec', '1')
}

do_patch:append:summitsom () {
    cp -af -t "${S}/arch/arm64/boot" "${UNPACKDIR}/dts"
}

# Build SDMA firmware into kernel
DEPENDS:append:summitsom:mx8m-generic-bsp = " firmware-imx"
do_compile:prepend:summitsom:mx8m-generic-bsp () {
    cp -af -t "${S}" "${STAGING_LIBDIR}/firmware"
}
