FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
	file://0002-gpio-add-gpio_of_helper.patch \
	file://0003-max310x-crystal-stabilize.patch \
	file://0004-Introduce-the-BQ25790-charger-driver.patch \
	file://0005-ts3a227.patch \
	file://0006-sound_ts3a227_support.patch \
	file://0008-fsl_aud2htx-probe.patch \
	file://0009-it6161.patch \
	file://0010-imx-sdma.patch \
	file://0011-fsl-sai.patch \
	file://0012-fsl-easrc.patch \
	file://0013-lcdifv3.patch \
	file://0014-phy-fsl-samsung-hdmi.patch \
	file://0015-imx8mp-hdmi-pavi.patch \
	file://0016-gpio-regulator-off-delay.patch \
	file://0017-fix-88w8997-pcie-enum.patch \
	file://0019-dm-verity-partition-wait-chromeos.patch \
	"

SRC_URI:append:imx8mp-summitsom = " \
	file://config/ \
	file://config/summitsom_defconfig \
	"

KBUILD_DEFCONFIG:remove = "${IMX_KERNEL_CONFIG_AARCH64}"

do_copy_defconfig:imx8mp-summitsom () {
	install -D -m 0644 -t ${S}/arch/arm64/boot/dts/freescale/ ${WORKDIR}/config/*.dts* 
}

RDEPENDS:${KERNEL_PACKAGE_NAME}-base:remove = "${KERNEL_PACKAGE_NAME}-image"
