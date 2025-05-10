
MACHINEOVERRIDES =. "summitsom:"

TI_PREFERRED_BSP = "ti-6_12"

BSP_BOOTLOADER_PROVIDER:bsp-ti-6_12 = "u-boot-ti-summit"
BSP_BOOTLOADER_VERSION:bsp-ti-6_12 = "%"

IMAGE_FSTYPES = ""

IMAGE_CLASSES += "dm-verity"

IMAGE_BOOT_FILES += "uboot.env ${KERNEL_IMAGETYPE}"
SWUPDATE_IMAGES = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin uboot.env ${KERNEL_IMAGETYPE}"

UBOOT_DTB_LOADADDRESS = "0x83800000"
UBOOT_DTBO_LOADADDRESS = "0x83880000"

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zst"
FIT_PAD_ALG = "pss"
FIT_HASH_ALG = "sha256"
FIT_SIGN_ALG = "rsa2048"
UBOOT_SIGN_KEYNAME = "dev"

UBOOT_SIGN_ENABLE:k3 = "1"
KERNEL_IMAGETYPE:k3 = "fitImage"

PREFERRED_RPROVIDER_wireless-regdb-static = "wireless-regdb"
PREFERRED_RPROVIDER_wpa-supplicant = "summit-supplicant-lwb-if"
PREFERRED_RPROVIDER_wpa-supplicant-cli = "summit-supplicant-lwb-if"
PREFERRED_RPROVIDER_wpa-supplicant-passphrase = "summit-supplicant-lwb-if"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager-lwb-if"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager-lwb-if-nmcli"

EMMC_DEVICE = "0"
SD_DEVICE = "1"
