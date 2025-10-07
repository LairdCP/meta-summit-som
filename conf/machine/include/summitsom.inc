MACHINEOVERRIDES =. "summitsom:"

KERNEL_CLASSES  = "kernel-fitimage"
KERNEL_IMAGETYPES = "fitImage"

KERNEL_IMAGETYPE = "fitImage"
KERNEL_IMAGETYPE:mx8-generic-bsp = "fitImage"
KERNEL_IMAGETYPE:mx9-generic-bsp = "fitImage"
IMAGE_CLASSES += "dm-verity"

UBOOT_DTB_LOADADDRESS = "0x43000000"
UBOOT_ENTRYPOINT:mx8m-generic-bsp = "0x40400000"

PREFERRED_RPROVIDER_wireless-regdb-static = "som8mp-radio-firmware"

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

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zst"

UBOOT_SIGN_ENABLE = "1"
UBOOT_SIGN_KEYNAME = "dev"
FIT_PAD_ALG = "pss"

UBOOT_SIGN_IMG_KEYNAME = "dev1"
FIT_SIGN_INDIVIDUAL = "1"

#UBOOT_ENCRYPT_ENABLE = "1"
UBOOT_ENCRYPT_KEYNAME = "key"
UBOOT_ENCRYPT_IVAME = "key-iv"
FIT_ENCRYPT_ALGO = "aes128"

EMMC_DEVICE = "2"
SD_DEVICE = "0"
