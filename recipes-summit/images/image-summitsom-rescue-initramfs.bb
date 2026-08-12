SUMMARY = "Summit SOM Rescue USB Boot initramfs Helper Image"
DESCRIPTION = "Summit SOM Rescue Manufacturing Provisioning USB Boot initramfs Helper Image"

## WARNING: This recipe is not intended to be built directly. 
# It is used as a helper for the summitsom-rescue-initramfs-main image.

LICENSE = "Ezurio-Clause"

inherit core-image

export IMAGE_BASENAME = "${PN}"

REQUIRED_DISTRO_FEATURES += "summitsom-rescue-initramfs"

# Required for use as INITRAMFS_IMAGE
INITRAMFS_FSTYPES = "cpio.zst"
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""

# Do not pollute the initramfs with unneeded rootfs features
IMAGE_FEATURES = "\
    allow-empty-password \
    allow-root-login \
    empty-root-password \
    "

# Minimal base packages
IMAGE_INSTALL += "\
    kernel-modules \
    ca-certificates \
    iproute2 \
    optee-client \
    dhcpcd \
    summit-update \
    ${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', 'summit-usbgadget', '', d)} \
    "

# Customization for provisioning init and serial auto-login
ROOTFS_POSTPROCESS_COMMAND:append = " enable_serial_autologin;"

enable_serial_autologin() {
    sed -i \
        -e 's,/usr/sbin/getty -L 115200.*,/bin/login -f root,g' \
        -e 's,/agetty ,/agetty -a root ,g' \
        "${IMAGE_ROOTFS}/etc/inittab"
}
