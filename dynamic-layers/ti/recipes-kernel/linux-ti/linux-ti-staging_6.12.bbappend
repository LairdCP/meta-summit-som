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
    file://0019-dm-verity-partition-wait-fix.patch \
    file://dts;subdir=git/arch/arm64/boot \
    "

KERNEL_DTBVENDORED:summitsom = "0"

# Remove kernel binary from rootfs
RDEPENDS:${KERNEL_PACKAGE_NAME}-base:remove:summitsom = "${KERNEL_PACKAGE_NAME}-image"
