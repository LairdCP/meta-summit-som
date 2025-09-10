DEFAULTTUNE:k3 ?= "armv8a-crc-crypto"

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

# Do not set these for k3r5
UBOOT_SIGN_ENABLE:k3 = "1"
KERNEL_IMAGETYPE:k3 = "fitImage"
KERNEL_IMAGETYPES:k3 = "fitImage"
IMAGE_BOOT_FILES:k3 = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS:remove = "kernel-image-image kernel-devicetree"

MACHINE_FEATURES += "rtc"
MACHINE_FEATURES:remove = "efi"

PREFERRED_PROVIDER_wpa-supplicant = "summit-supplicant"
PREFERRED_RPROVIDER_wpa-supplicant = "summit-supplicant"
PREFERRED_RPROVIDER_wpa-supplicant-cli = "summit-supplicant"
PREFERRED_RPROVIDER_wpa-supplicant-passphrase = "summit-supplicant"
PREFERRED_RPROVIDER_wpa-supplicant-lib = "summit-supplicant"
PREFERRED_RPROVIDER_wpa_supplicant-dev = "summit-supplicant-dev"

PREFERRED_PROVIDER_hostapd = "summit-hostapd"
PREFERRED_RPROVIDER_hostapd = "summit-hostapd"

PREFERRED_PROVIDER_networkmanager = "summit-networkmanager"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager-nmcli"

EMMC_DEVICE = "0"
SD_DEVICE = "1"

SERIAL_CONSOLES = "115200;ttyS2"
