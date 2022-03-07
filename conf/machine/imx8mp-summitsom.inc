#@TYPE: Machine
#@NAME: Laird Connectivity i.MX8M Plus Summit SOM
#@SOC: i.MX8
#@DESCRIPTION: Machine configuration for Laird Connectivity i.MX8M Plus Summit SOM
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@lairdconnect.com>

MACHINEOVERRIDES =. "mx8:mx8m:mx8mp:summitsom:"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

MACHINE_FEATURES += "pci usbhost alsa rtc"

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zstd"

KERNEL_CLASSES  = "kernel-fitimage"
KERNEL_IMAGETYPES = "fitImage"

KERNEL_IMAGETYPE_summitsom = "fitImage"
IMAGE_BOOT_FILES = "${KERNEL_IMAGETYPE}"

IMAGE_CLASSES = "squashfs-zstd dm-verity"

UBOOT_DTB_LOADADDRESS = "0x43000000"
UBOOT_ENTRYPOINT_mx8m   = "0x40400000"

WKS_FILES ?= "lrd-imx-uboot-spl-bootpart.wks.in"

KERNEL_DEVICETREE = " \
    freescale/imx8mp-summitsom-dvk-pcie-uart.dtb \
    freescale/imx8mp-summitsom-dvk-sdio-uart.dtb \
    freescale/imx8mp-summitsom-dvk-sdio-uart-stop.dtb \
    freescale/imx8mp-summitsom-dvk-sdio-uart-susp.dtb \
    freescale/imx8mp-summitsom-dvk-sdio-sdio.dtb \
    freescale/imx8mp-summitsom-wbx-pcie-uart.dtb \
    freescale/imx8mp-summitsom-wbx-sdio-uart.dtb \
    freescale/imx8mp-summitsom-wbx-sdio-uart-stop.dtb \
    freescale/imx8mp-summitsom-wbx-sdio-uart-susp.dtb \
    freescale/imx8mp-summitsom-wbx-sdio-sdio.dtb \
    "

UBOOT_DTB_NAME = "imx8mp-summitsom-dvk.dtb"

UBOOT_CONFIG ??= "sd"
UBOOT_CONFIG[sd] = "${UBOOT_CONFIG_BASENAME}_defconfig,sdcard"
UBOOT_CONFIG[fspi] = "${UBOOT_CONFIG_BASENAME}_defconfig"
UBOOT_CONFIG[mfgtool] = "${UBOOT_CONFIG_BASENAME}_defconfig"

# Set DDR FIRMWARE
DDR_FIRMWARE_VERSION = "202006"
DDR_FIRMWARE_NAME = " \
	lpddr4_pmu_train_1d_dmem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_1d_imem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_2d_dmem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_2d_imem_${DDR_FIRMWARE_VERSION}.bin \
"

SPL_BINARY = "spl/u-boot-spl.bin"

ATF_PLATFORM = "imx8mp"
ATF_LOAD_ADDR = "0x970000"

IMX_EXTRA_FIRMWARE = "firmware-imx-8m"

IMXBOOT_TARGETS = \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', '${IMXBOOT_TARGETS_BASENAME}_flexspi', \
                                                  '${IMXBOOT_TARGETS_BASENAME}', d)}"

IMX_BOOT_SOC_TARGET = "iMX8MP"

SERIAL_CONSOLES = "115200;ttymxc1"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = "all"
IMX_BOOT_SEEK = "32"

OPTEE_BIN_EXT = "8mp"
TEE_LOAD_ADDR = "0x56000000"

MACHINE_EXTRA_RDEPENDS += "${MACHINE_FIRMWARE} kernel-modules"

IMXBOOT_TARGETS_BASENAME = "flash_evk"

PREFERRED_RPROVIDER_wireless-regdb-static = "summit60-firmware-som"
PREFERRED_RPROVIDER_wpa-supplicant = "summit-supplicant-som"
PREFERRED_RPROVIDER_wpa-supplicant-cli = "summit-supplicant-som"
PREFERRED_RPROVIDER_wpa-supplicant-passphrase = "summit-supplicant-som"

MACHINE_FEATURES_remove = "nxp8987 nxp8997"
MACHINE_FIRMWARE_remove = "linux-firmware-ath10k"
