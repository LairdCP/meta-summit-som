DESCRIPTION = "Summit SOM Minimal WBx3 Initramfs Rootfs"
SUMMARY = "Minimal rootfs for embedding in kernel as initramfs"

REQUIRED_DISTRO_FEATURES:append = " \
    summitsom-wbx3-initramfs \
    "

inherit core-image

# Required for use as INITRAMFS_IMAGE
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_LINGUAS = ""

# Don't allow the initramfs to contain a kernel image
PACKAGE_EXCLUDE = "kernel-image-*"

# Do not pollute the initramfs with unneeded rootfs features
IMAGE_FEATURES = ""

INITRAMFS_MAXSIZE = "524288"

# Minimal base packages
IMAGE_INSTALL += "\
    kernel-modules \
    ca-certificates \
    tzdata-core \
    iproute2 \
    gptfdisk \
    optee-client \
    libgpiod \
    libgpiod-tools \
    summit-automount \
    summit-update \
    summit-initdata \
    ${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', 'summit-usbgadget', '', d)} \
    "

# Trim optional recommendations that add significant size but are not needed
# for provisioning in this initramfs.
BAD_RECOMMENDATIONS += " \
    udev-hwdb \
    kbd \
    kbd-consolefonts \
    kbd-keymaps \
    kbd-keymaps-pine \
    optee-test \
    "

# Remove features we don't need (alsa, wifi, bluetooth already in distro)
IMAGE_FEATURES:remove = "ssh-server-dropbear"

# Customization for provisioning init and serial auto-login
ROOTFS_POSTPROCESS_COMMAND:append = " init_provisioning_script; enable_serial_autologin;"

init_provisioning_script() {
    cat > "${IMAGE_ROOTFS}/init" << 'EOF'
#!/bin/sh
mount -t proc proc /proc
mount -t sysfs sysfs /sys
mount -t devtmpfs devtmpfs /dev
exec /sbin/init
EOF
    chmod 0755 "${IMAGE_ROOTFS}/init"
}

CONSOLE_DEV ?= "ttyLP0"

enable_serial_autologin() {
    # Install a tiny autologin helper that getty invokes instead of /bin/login
    cat > "${IMAGE_ROOTFS}/sbin/autologin" << 'AUTOLOGIN'
#!/bin/sh
exec /bin/login -f root
AUTOLOGIN
    chmod 0755 "${IMAGE_ROOTFS}/sbin/autologin"

    # Replace any existing getty / shell entries for the serial console
    if [ -f "${IMAGE_ROOTFS}/etc/inittab" ]; then
        sed -i '/^S0:/d' "${IMAGE_ROOTFS}/etc/inittab"
        sed -i '\|${CONSOLE_DEV}|d' "${IMAGE_ROOTFS}/etc/inittab"
    fi
    echo 'S0:12345:respawn:/sbin/getty -L -n -l /sbin/autologin 115200 ${CONSOLE_DEV} vt100' >> "${IMAGE_ROOTFS}/etc/inittab"
}
