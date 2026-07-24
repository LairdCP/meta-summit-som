#@TYPE: Machine
#@NAME: Carbon AM6xx
#@SOC: AM62x, AM62Lx, AM67x
#@DESCRIPTION: Machine configuration for Carbon AM6xx
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@ezurio.com>

MACHINEOVERRIDES =. "summitsom:"

# for better sstate cache reuse
DEFAULTTUNE:k3 = "armv8a-crc-crypto"

require conf/machine/include/u-boot-summit.inc
include ${@bb.utils.contains('BBFILE_COLLECTIONS', 'summit-radio', 'conf/machine/include/radio-stack.inc', '', d)}

MACHINE_FEATURES:k3r5 = ""
MACHINE_FEATURES:k3 = "usbgadget usbhost alsa rtc optee screen wifi bluetooth camera"

TI_PREFERRED_BSP = "ti-6_18"

SWUPDATE_IMAGES = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin uboot.env fitImage"

BSP_BOOTLOADER_PROVIDER:bsp-ti-6_18 = "u-boot-ti-summit"
BSP_BOOTLOADER_VERSION:bsp-ti-6_18 = "%"

SERIAL_CONSOLES = "115200;ttyS2"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS:k3 = ""
MACHINE_EXTRA_RDEPENDS:k3 = "kernel-modules"
EXTRA_IMAGEDEPENDS:k3 = "virtual/bootloader ti-kernel-fitimage"

IMAGE_FSTYPES = ""

IMAGE_CLASSES += "dm-verity"

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zst"

FIT_PAD_ALG = "pss"
FIT_HASH_ALG = "sha512"
FIT_SIGN_ALG = "rsa4096"

UBOOT_SIGN_KEYDIR = "${SUMMIT_SOM_KEYS_DIR}/carbon"
UBOOT_SIGN_KEYNAME = "dev"

# Do not set these for k3r5
UBOOT_SIGN_ENABLE:k3 = "1"
KERNEL_IMAGETYPE:k3 = "Image"
KERNEL_IMAGETYPES:k3 = "Image"
IMAGE_BOOT_FILES:k3 = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin uboot.env fitImage"

UBOOT_ENV_OFFSET = "3670016"
EMMC_SPL_OFFSET = "524288"
EMMC_BOOT_OFFSET = "2621440"
EMMC_DEVICE = "0"
SD_DEVICE = "1"
