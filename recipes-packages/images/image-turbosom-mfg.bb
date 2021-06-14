DESCRIPTION = "Turbo SOM Radio Manufacturing Image"
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
	ca-certificates \
	tzdata \
	ethtool \
	iproute2 \
	chrony \
	chronyc \
	packagegroup-imx-tools-audio \
	kernel-module-pca193x \
	kernel-module-lrd-wm8960 \
	packagegroup-radio-stack \
	lrd-automount \
	lrd-initdata \
	lrd-usbgadget \
	lrd-bt-uart-scripts \
	u-boot-imx-fw-utils \
	mpg123 \
	"

# Diagnostic tools
IMAGE_INSTALL += "\
	hub-ctrl \
	iperf3 \
	htop \
	tcpdump \
	usbutils \
	pciutils \
	picocom \
	can-utils \
	stress-ng \
	"

# Radio manufacturing components
IMAGE_INSTALL += "\
	packagegroup-radio-stack-mfg \
	packagegroup-radio-stack-mfgbridge-8997 \
	"

