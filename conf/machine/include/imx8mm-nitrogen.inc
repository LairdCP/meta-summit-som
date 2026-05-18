#@TYPE: Machine
#@NAME: Nitrogen i.MX8M Mini
#@SOC: i.MX8
#@DESCRIPTION: Machine configuration for Nitrogen i.MX8M Mini
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@ezurio.com>

MACHINEOVERRIDES =. "mx8mm:summitsom:"

require conf/machine/include/imx-base.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

require conf/machine/include/u-boot-summit.inc
include ${@bb.utils.contains('BBFILE_COLLECTIONS', 'summit-radio', 'conf/machine/include/radio-stack.inc', '', d)}

DEFAULTTUNE:mx8m-generic-bsp = "armv8a-crc-crypto"

MACHINE_FEATURES += "pci usbhost alsa rtc optee wifi bluetooth camera"

SOC_DEFAULT_IMAGE_FSTYPES = ""

IMAGE_BOOT_FILES = "uboot.env ${KERNEL_IMAGETYPE}"
SWUPDATE_IMAGES = "flash.bin uboot.env ${KERNEL_IMAGETYPE}"

IMX_DEFAULT_BOOTLOADER:mx8m-generic-bsp = "u-boot-imx-summit"
IMX_DEFAULT_UBOOTTOOLS:imx-generic-bsp = "u-boot-summit-tools"

DDR_FIRMWARE_NAME = " \
    lpddr4_pmu_train_1d_dmem.bin \
    lpddr4_pmu_train_1d_imem.bin \
    lpddr4_pmu_train_2d_dmem.bin \
    lpddr4_pmu_train_2d_imem.bin \
"

SPL_BINARY = "spl/u-boot-spl.bin"

ATF_PLATFORM = "imx8mm"

SERIAL_CONSOLES = "115200;ttymxc1"

LOADADDR = ""
UBOOT_SUFFIX = "img"
UBOOT_BINARY = "flash.bin"
UBOOT_MAKE_TARGET = "all"
IMX_BOOT_SEEK = "33"

OPTEE_BIN_EXT = "8mm"
TEE_LOAD_ADDR = "0xbe000000"

MACHINE_EXTRA_RDEPENDS += "${MACHINE_FIRMWARE} kernel-modules"

IMX_DEFAULT_BSP = "nxp"

MACHINE_EXTRA_RRECOMMENDS:remove = "imx-m4-demos"

KERNEL_CLASSES  = "kernel-fitimage"
KERNEL_IMAGETYPES = "fitImage"

KERNEL_IMAGETYPE = "fitImage"
KERNEL_IMAGETYPE:mx8-generic-bsp = "fitImage"
IMAGE_CLASSES += "dm-verity"

UBOOT_DTB_LOADADDRESS = "0x43000000"
UBOOT_ENTRYPOINT:mx8m-generic-bsp = "0x40400000"

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zst"

FIT_PAD_ALG = "pss"
FIT_HASH_ALG = "sha256"
FIT_SIGN_ALG = "rsa2048"

CUSTOM_KEYS_DIR ?= ""
UBOOT_SIGN_KEYDIR = "${@oe.utils.conditional('CUSTOM_KEYS_DIR', '', '${SUMMIT_SOM_KEYS_DIR}/summitsom', '${CUSTOM_KEYS_DIR}', d)}"
UBOOT_SIGN_KEYNAME = "dev"
UBOOT_SIGN_ENABLE = "1"

EMMC_BOOT_OFFSET = "33K"
EMMC_DEVICE = "0"
SD_DEVICE = "1"
