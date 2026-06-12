#@TYPE: Machine
#@NAME: Nitrogen i.MX8M Plus
#@SOC: i.MX8
#@DESCRIPTION: Machine configuration for Nitrogen i.MX8M Plus
#@MAINTAINER: Boris Krasnovskiy <Boris.Krasnovskiy@ezurio.com>

require conf/machine/include/imx8mp-summitsom.inc

UBOOT_SIGN_KEYDIR = "${SIG_DATA_PATH}/keys"
SIG_DATA_PATH ?= "${SUMMIT_SOM_KEYS_DIR}/nitrogen/hab4"

EMMC_DEVICE = "0"
SD_DEVICE = "1"
