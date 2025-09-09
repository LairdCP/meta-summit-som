FILESEXTRAPATHS:prepend:summitsom := "${THISDIR}/${PN}-6.12:"

SRC_URI:append:summitsom = " \
    file://0001-ts3a227-jack.patch \
    file://0002-ts3a227-irq.patch \
    file://0004-simple_card_ts3a227.patch \
    file://0006-dp83867.patch \
    file://0007-gpio-pca953x-pullup.patch \
    file://0008-hwmon-ina2xx-add-support-for-ina232.patch \
    file://0009-dt-bindings-hwmon-ina2xx-add-ina232.patch \
    file://0011-lvds-codec-output-bridge.patch \
    file://0012-lvds-codec-frequency-limit.patch \
    file://0013-FROMLIST-regulator-tps65219-Fix-devm_kmalloc-size-al.patch \
    file://0019-dm-verity-partition-wait-fix.patch \
    file://0020-gpio-pca953x-Add-support-for-TI-TCA6418-GPIO-chip.patch \
    file://0021-tps6287x-regulator.patch \
    file://0022-usb-common-usb-conn-gpio-use-a-unique-name-for-usb-c.patch \
    file://0023-am67-add-peripherals.patch \
    file://0025-UPSTREAM-arm64-dts-ti-k3-pinctrl-Enable-Schmitt-Trig.patch \
    file://0026-dp83867-irq.patch \
    file://0027-davinci-mdio-missing-cpu.patch \
    file://0028-tidss-rgb18-limit.patch \
    file://0030-tps65219-trap.patch \
    file://0031-PENDING-arch-arm64-dts-ti-Move-the-companion-and-sec.patch \
    file://0032-PENDING-arm64-dts-ti-k3-am62p-j722s-common-main-Fix-.patch \
    file://0033-PENDING-arm64-dts-ti-k3-j722s-main-Fix-interrupts-pr.patch \
    file://0034-PENDING-arm64-dts-ti-k3-am62-main-Fix-interrupts-pro.patch \
    file://0035-Revert-drm-bridge-cdns-dsi-core-Remove-empty-FIFO-ch.patch \
    file://0036-drm-bridge-cdns-dsi-Make-it-work-a-bit-better.patch \
    file://0037-phy-cdns-dphy-Store-hs_clk_rate-and-return-it.patch \
    file://0038-phy-cdns-dphy-Remove-leftover-code.patch \
    file://0040-k3-am62p-j722s-remove-duplicate-dphy0.patch \
    file://0041-PENDING-media-ti-j721e-csi2rx-Serialize-stream-stops.patch \
    file://0042-PENDING-media-ti-j721e-csi2rx-Remove-word-size-align.patch \
    file://0043-gpio-pca953x-fix-IRQ-storm-on-system-wake-up.patch \
    file://0044-gpio-pca953x-fix-wrong-error-probe-return-value.patch \
    file://0045-mcan-suspend-crash.patch \
    file://0046-gpio-keys-resume-error.patch \
    file://0047-media-ov5645-Add-support-for-streams.patch \
    file://0048-media-ov5640-digient-rc-delay-compensation.patch \
    file://0049-gpio-pca953x-Add-support-for-level-triggered-interru.patch \
    file://0050-gpio-pca953x-use-regmap_update_bits-to-improve-perfo.patch \
    file://0051-Input-goodix-add-support-for-polling-mode.patch \
    file://0052-goodix-fix-lost-irqs.patch \
    "

SRC_URI:append:summitsom = " \
    file://dts;subdir=git/arch/arm64/boot \
    "

KERNEL_DTBVENDORED:summitsom = "0"
KERNEL_DEFCONFIG:summitsom = "file://${KERNEL_DEFCONFIG_SUMMIT}"

KERNEL_DEFCONFIG_SUMMIT:am62xx = "k3-am625-carbon_defconfig"
KERNEL_DEFCONFIG_SUMMIT:j722s  = "k3-am675-carbon_defconfig"

do_configure:prepend:summitsom() {
    cp -f "${WORKDIR}/${KERNEL_DEFCONFIG_SUMMIT}" "${WORKDIR}/defconfig"
}

# Remove kernel binary from rootfs
RRECOMMENDS:${KERNEL_PACKAGE_NAME}-base:summitsom = ""
