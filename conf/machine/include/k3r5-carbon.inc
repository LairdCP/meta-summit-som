#@TYPE: Machine
#@NAME: Carbon AM6xx
#@SOC: AM62x, AM67x
#@DESCRIPTION: Machine configuration for Carbon AM6xx R5
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@ezurio.com>

MACHINEOVERRIDES =. "summitsom:"

require conf/machine/include/u-boot-summit.inc

TI_PREFERRED_BSP = "ti-6_18"

SERIAL_CONSOLES = "115200;ttyS2"

FIT_PAD_ALG = "pss"
FIT_HASH_ALG = "sha512"
FIT_SIGN_ALG = "rsa4096"

UBOOT_SIGN_KEYDIR = "${SUMMIT_SOM_KEYS_DIR}/carbon"
UBOOT_SIGN_KEYNAME = "dev"

UBOOT_ENV_OFFSET = "3670016"
EMMC_SPL_OFFSET = "524288"
EMMC_BOOT_OFFSET = "2621440"
EMMC_DEVICE = "0"
SD_DEVICE = "1"
