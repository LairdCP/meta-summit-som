MACHINEOVERRIDES =. "summitsom:"

KERNEL_CLASSES  = "kernel-fitimage"
KERNEL_IMAGETYPES = "fitImage"

KERNEL_IMAGETYPE:summitsom = "fitImage"
IMAGE_CLASSES += "squashfs-zstd dm-verity"

UBOOT_DTB_LOADADDRESS = "0x43000000"
UBOOT_ENTRYPOINT_mx8m   = "0x40400000"

PREFERRED_RPROVIDER_wireless-regdb-static = "som8mp-radio-firmware"
PREFERRED_RPROVIDER_wpa-supplicant = "summit-supplicant-som"
PREFERRED_RPROVIDER_wpa-supplicant-cli = "summit-supplicant-som"
PREFERRED_RPROVIDER_wpa-supplicant-passphrase = "summit-supplicant-som"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager-som"
PREFERRED_RPROVIDER_networkmanager = "summit-networkmanager-som-nmcli"

MACHINE_FEATURES += "qemu-usermode"

FIT_KERNEL_COMP_ALG = "zstd"
FIT_KERNEL_COMP_ALG_EXTENSION = ".zstd"
FIT_SIGN_INDIVIDUAL = "1"
FIT_PAD_ALG = "pss"

UBOOT_SIGN_KEYNAME = "dev"
UBOOT_SIGN_IMG_KEYNAME = "dev1"
UBOOT_SIGN_ENABLE = "1"
