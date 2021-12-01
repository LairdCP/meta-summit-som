DESCRIPTION = "Summit SOM Command Line Image"
LICENSE = "MIT"

inherit core-image

export IMAGE_BASENAME = "${PN}"

IMAGE_FEATURES += "\
	ssh-server-dropbear \
	"

IMAGE_FEATURES_remove = "\
	tools-profile \
	tools-debug \
	tools-testapps \
	"

IMAGE_INSTALL += "\
	kernel-module-pac193x \
	packagegroup-radio-stack \
	ca-certificates \
	tzdata \
	ethtool \
	iproute2 \
	chrony \
	chronyc \
	packagegroup-fsl-tools-audio \
	lrd-automount \
	lrd-initdata \
	lrd-usbgadget \
	u-boot-fw-utils \
	mpg123 \
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
	"
