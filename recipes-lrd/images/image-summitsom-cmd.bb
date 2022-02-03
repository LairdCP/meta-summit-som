DESCRIPTION = "Summit SOM Command Line Image"
LICENSE = "MIT"

inherit core-image extrausers

EXTRA_USERS_PARAMS = "usermod -P summit root;"

export IMAGE_BASENAME = "${PN}"

ROOTFS_POSTPROCESS_COMMAND += "clean_boot_dir;"

clean_boot_dir() {
    rm -rf ${IMAGE_ROOTFS}/boot/*
}

IMAGE_FEATURES = "\
	ssh-server-dropbear \
	allow-root-login \
	"

IMAGE_INSTALL += "\
	kernel-module-pac193x \
	packagegroup-radio-stack \
	ca-certificates \
	tzdata \
	ethtool \
	iproute2 \
	linuxptp \
	chrony \
	chronyc \
	packagegroup-fsl-tools-audio \
	lrd-automount \
	lrd-initdata \
	lrd-usbgadget \
	u-boot-fw-utils \
	swupdate \
	mpg123 \
	read-edid \
	fbida \
	keyctl-caam \
	crypto-af-alg \
	keyutils \
	lvm2 \
	"

# Diagnostic tools
IMAGE_INSTALL += "\
	hub-ctrl \
	iperf2 \
	iperf3 \
	htop \
	tcpdump \
	usbutils \
	pciutils \
	picocom \
	can-utils \
	stress-ng \
	mc-mint \
	"
