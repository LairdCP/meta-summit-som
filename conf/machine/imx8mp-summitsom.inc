#@TYPE: Machine
#@NAME: Laird Connectivity i.MX8M Plus Summit SOM
#@SOC: i.MX8
#@DESCRIPTION: Machine configuration for Laird Connectivity i.MX8M Plus Summit SOM
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@lairdconnect.com>

MACHINEOVERRIDES =. "mx8:mx8m:mx8mp:"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc
require summitsom.inc

MACHINE_FEATURES += "pci usbhost alsa rtc optee"

IMAGE_BOOT_FILES_remove = "${@make_dtb_boot_files(d)} ${OPTEE_BOOT_IMAGE}"

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

MACHINE_FEATURES_remove = "nxp8987 nxp8997"
MACHINE_FIRMWARE_remove = "linux-firmware-ath10k"
MACHINE_EXTRA_RRECOMMENDS_remove = "imx-m7-demos"
