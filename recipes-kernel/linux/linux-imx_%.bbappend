FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://config/turbosom_defconfig \
	file://config/imx8mp-turbosom.dtsi \
	file://config/imx8mp-turbosom-pcie-uart.dtsi \
	file://config/imx8mp-turbosom-sdio-sdio.dtsi \
	file://config/imx8mp-turbosom-sdio-uart.dtsi \
	file://config/imx8mp-turbosom-media-dvk.dtsi \
	file://config/imx8mp-turbosom-dvk.dtsi \
	file://config/imx8mp-turbosom-dvk-sdio.dts \
	file://config/imx8mp-turbosom-dvk-pcie.dts \
	file://config/imx8mp-turbosom-mfg.dtsi \
	file://config/imx8mp-turbosom-mfg-sdio.dts \
	file://config/imx8mp-turbosom-mfg-pcie.dts \
	file://config/imx8mp-turbosom-gpu.dtsi \
	file://config/imx8mp-turbosom-mipi-dvk.dtsi \
	file://config/imx8mp-turbosom-hdmi-dvk.dtsi \
	file://config/imx8mp-turbosom-lvds-bridge-dvk.dtsi \
	\
	file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-.patch \
	file://0002-gpio-add-gpio_of_helper.patch \
	file://0003-pca9450-kconfig.patch \
	file://0004-spi-imx-fsl-lpspi-Convert-to-GPIO-descriptors.patch \
	file://0005-max310x-crystal-stabilize.patch \
	file://0006-power-supply-core-Update-sysfs-class-power-ABI-docum.patch \
	file://0007-power-supply-core-add-POWER_SUPPLY_HEALTH_CALIBRATIO.patch \
	file://0008-power_supply-Add-additional-health-properties-to-the.patch \
	file://0009-Introduce-the-BQ25790-charger-driver.patch \
	file://0010-pwrseq-sd8787.patch \
	file://0011-usb-gadget-f_ncm-fix-ncm_bitrate-for-SuperSpeed-and-.patch \
	file://0012-usb-gadget-f_ncm-set-SuperSpeed-bulk-descriptor-bMax.patch \
	file://0013-usb-gadget-f_ncm-allow-using-NCM-in-SuperSpeed-Plus-.patch \
	file://0014-USB-gadget-f_rndis-fix-bitrate-for-SuperSpeed-and-ab.patch \
	file://0101-usb-dwc3-ep0-Fix-delay-status-handling.patch \
	file://0102-usb-dwc3-gadget-Continue-to-process-pending-requests.patch \
	file://0103-usb-dwc3-gadget-Reclaim-extra-TRBs-after-request-com.patch \
	file://0104-usb-dwc3-pci-add-support-for-the-Intel-Alder-Lake-S.patch \
	file://0105-usb-dwc3-ulpi-Use-VStsDone-to-detect-PHY-regs-access.patch \
	file://0106-usb-dwc3-fix-clock-issue-during-resume-in-OTG-mode.patch \
	file://0107-usb-dwc3-ulpi-fix-checkpatch-warning.patch \
	file://0108-usb-dwc3-ulpi-Replace-CPU-based-busyloop-with-Protoc.patch \
	file://0109-usb-dwc3-gadget-Fix-setting-of-DEPCFG.bInterval_m1.patch \
	file://0110-usb-dwc3-gadget-Fix-dep-interval-for-fullspeed-inter.patch \
	file://0111-usb-dwc3-qcom-Add-missing-DWC3-OF-node-refcount-decr.patch \
	file://0112-usb-dwc3-qcom-Honor-wakeup-enabled-disabled-state.patch \
	file://0114-ts3a227.patch \
	file://0115-hdmi.patch \
	"

DEFCONFIG ?= "turbosom_defconfig"

do_copy_defconfig () {
	install -d ${B}
	cp ${WORKDIR}/config/${DEFCONFIG} ${B}/.config
	cp ${WORKDIR}/config/${DEFCONFIG} ${B}/../defconfig
	cp ${WORKDIR}/config/*.dts* ${S}/arch/arm64/boot/dts/freescale/
}
