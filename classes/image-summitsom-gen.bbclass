LICENSE = "MIT"

inherit core-image extrausers

PASSWD = "\$5\$JJ/ksbVr4475qA49\$wzyEBumoH1YyHOG9OgKzjRKjwGVImxFDCu0m90hymoA"

EXTRA_USERS_PARAMS = "usermod -p '${PASSWD}' root;"

export IMAGE_BASENAME = "${PN}"

IMAGE_FSTYPES = "squashfs-zstd.verity"

IMAGE_ROOTFS_EXTRA_SPACE = "0"

IMAGE_FEATURES = "\
	ssh-server-dropbear \
	allow-root-login \
	"

IMAGE_FEATURES_append_lrdsecure += "\
	read-only-rootfs \
	"

IMAGE_INSTALL_BASIC = "\
	kernel-module-pac193x \
	packagegroup-radio-stack \
	ca-certificates \
	tzdata-core \
	tzdata-posix \
	iproute2 \
	chrony \
	chronyc \
	summit-automount \
	summit-initdata \
	summit-update \
	summit-fwenv \
	summit-usbgadget \
	"

# Diagnostic tools
IMAGE_INSTALL_DIAG = "\
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

ROOTFS_POSTPROCESS_COMMAND += "rootfs_os_release; "

SUMMIT_VERSION ?= "0.0.0.0"

rootfs_os_release() {
	sed -i -e 's,ID=os-release,ID=${IMAGE_BASENAME},g' ${IMAGE_ROOTFS}${libdir}/os-release
	echo 'Summit SOM ${IMAGE_BASENAME} ${SUMMIT_VERSION} \\n \l' > ${IMAGE_ROOTFS}${sysconfdir}/issue
	echo 'Summit SOM ${IMAGE_BASENAME} ${SUMMIT_VERSION} %h' > ${IMAGE_ROOTFS}${sysconfdir}/issue.net
}

ARCHIVE_NAME ?= "${IMAGE_BASENAME}-summit-${SUMMIT_VERSION}"
ARCHIVE_WILDCARD ?= ""

addtask create_archive after do_image_complete before do_build

do_create_archive() {
	if [ "${SUMMIT_VERSION}" != "0.0.0.0" ]; then
		tar --transform='s,.*/,,' -cjf ${DEPLOY_DIR_IMAGE}/${ARCHIVE_NAME}.tar ${ARCHIVE_WILDCARD}
	fi
}
