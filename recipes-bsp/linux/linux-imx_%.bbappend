FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://config/summitsom_defconfig \
	file://config/imx8mp-summitsom.dtsi \
	file://config/imx8mp-summitsom-pcie-uart.dtsi \
	file://config/imx8mp-summitsom-sdio-sdio.dtsi \
	file://config/imx8mp-summitsom-sdio-uart.dtsi \
	file://config/imx8mp-summitsom-media-dvk.dtsi \
	file://config/imx8mp-summitsom-gpu.dtsi \
	file://config/imx8mp-summitsom-mipi-dvk.dtsi \
	file://config/imx8mp-summitsom-hdmi-dvk.dtsi \
	file://config/imx8mp-summitsom-lvds-bridge-dvk.dtsi \
	file://config/imx8mp-summitsom-dvk.dtsi \
	file://config/imx8mp-summitsom-dvk-sdio-uart.dts \
	file://config/imx8mp-summitsom-dvk-sdio-sdio.dts \
	file://config/imx8mp-summitsom-dvk-pcie-uart.dts \
	file://config/imx8mp-summitsom-wbx.dtsi \
	file://config/imx8mp-summitsom-wbx-sdio-uart.dts \
	file://config/imx8mp-summitsom-wbx-sdio-sdio.dts \
	file://config/imx8mp-summitsom-wbx-pcie-uart.dts \
	\
	file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
	file://0002-gpio-add-gpio_of_helper.patch \
	file://0003-max310x-crystal-stabilize.patch \
	file://0004-Introduce-the-BQ25790-charger-driver.patch \
	file://0005-ts3a227.patch \
	file://0006-sound_ts3a227_support.patch \
	file://0007-hdmi.patch \
	file://0008-fsl_aud2htx-probe.patch \
	file://0009-it6161.patch \
	file://0010-imx-sdma.patch \
	file://0011-fsl-sai.patch \
	file://0012-fsl-easrc.patch \
	file://0013-lcdifv3.patch \
	file://0014-phy-fsl-samsung-hdmi.patch \
	file://0015-imx8mp-hdmi-pavi.patch \
	"

KBUILD_DEFCONFIG_remove = "${IMX_KERNEL_CONFIG_AARCH64}"

do_copy_defconfig () {
	install -d ${B}
	cp ${WORKDIR}/config/*.dts* ${S}/arch/arm64/boot/dts/freescale/
}
