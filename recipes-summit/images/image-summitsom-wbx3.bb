SUMMARY = "Summit SOM WBx3 SD Card Boot Image"
DESCRIPTION = "Summit SOM WBx3 Manufacturing Provisioning SD Card Boot Image"

REQUIRED_DISTRO_FEATURES += "summitsom-wbx3"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES = "\
    allow-empty-password \
    allow-root-login \
    empty-root-password \
    "

IMAGE_INSTALL += "\
    ca-certificates \
    iproute2 \
    optee-client \
    summit-update \
    summit-initdata \
    summit-usbgadget \
    ${VIRTUAL-RUNTIME_base-utils-syslog} \
    "

# Customization for provisioning init and serial auto-login
ROOTFS_POSTPROCESS_COMMAND:append = " enable_serial_autologin; cleanup_rootfs;"

enable_serial_autologin() {
    sed -i \
        -e 's,/usr/sbin/getty 115200.*,/bin/login -f root,g' \
        -e 's,/agetty ,/agetty -a root ,g' \
        "${IMAGE_ROOTFS}/etc/inittab"
}

cleanup_rootfs() {
    chmod -x "${IMAGE_ROOTFS}/etc/init.d/populate-volatile.sh"
}
