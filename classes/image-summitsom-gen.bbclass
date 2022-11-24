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

IMAGE_FEATURES:append:summit-secure = "\
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

IMAGE_INSTALL_BASIC:append:summit-secure = "\
	keyctl-caam \
	keyutils \
	lvm2 \
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
		tar --transform='s,.*/,,' -cjf ${DEPLOY_DIR_IMAGE}/${ARCHIVE_NAME}.tar.bz2 ${ARCHIVE_WILDCARD}
	fi
}

do_backup_runtime () {
	BACKUP_SECRET_DIR=${IMAGE_ROOTFS}/usr/share/factory/etc/secret
	BACKUP_MISC_DIR=${IMAGE_ROOTFS}/usr/share/factory/etc/misc

	mkdir -p ${BACKUP_SECRET_DIR}
	for BACKUP_TARGET in "firewalld" "weblcm-python" "modem" "stunnel"; do
		if [ -d ${IMAGE_ROOTFS}/etc/"${BACKUP_TARGET}" ]; then
			mv ${IMAGE_ROOTFS}/etc/${BACKUP_TARGET}/ ${BACKUP_SECRET_DIR}
			ln -sf /data/secret/${BACKUP_TARGET} ${IMAGE_ROOTFS}/etc/${BACKUP_TARGET}
		fi
	done

	mkdir -p ${BACKUP_SECRET_DIR}/NetworkManager
	for SM_SUB_DIR in "certs" "system-connections"; do
		if [ -d ${IMAGE_ROOTFS}/etc/NetworkManager/${SM_SUB_DIR} ]; then
			mv ${IMAGE_ROOTFS}/etc/NetworkManager/${SM_SUB_DIR} ${BACKUP_SECRET_DIR}/NetworkManager
		else
			mkdir -p ${BACKUP_SECRET_DIR}/NetworkManager/${SM_SUB_DIR}
		fi
		ln -sf /data/secret/NetworkManager/${SM_SUB_DIR} ${IMAGE_ROOTFS}/etc/NetworkManager/${SM_SUB_DIR}
	done

	ln -sf /data/secret/NetworkManager.state ${IMAGE_ROOTFS}/etc/NetworkManager/NetworkManager.state

	mkdir -p ${BACKUP_MISC_DIR}
	mv -t ${BACKUP_MISC_DIR} \
		${IMAGE_ROOTFS}/etc/timezone \
		${IMAGE_ROOTFS}/etc/localtime \
		${IMAGE_ROOTFS}/etc/adjtime

	ln -sf /data/misc/timezone ${IMAGE_ROOTFS}/etc/timezone
	ln -sf /data/misc/localtime ${IMAGE_ROOTFS}/etc/localtime
	ln -sf /data/misc/adjtime ${IMAGE_ROOTFS}/etc/adjtime

	# Needed to satisfy preset_all on some rebuilds
	rm -rf ${IMAGE_ROOTFS}/etc/machine-id

	rm -rf ${IMAGE_ROOTFS}/media
	ln -sf /run/media ${IMAGE_ROOTFS}/media
}

ROOTFS_POSTPROCESS_COMMAND:append:summit-secure = "do_backup_runtime; "

do_machine_id () {
	ln -sf /perm/etc/machine-id ${IMAGE_ROOTFS}/etc/machine-id
}

IMAGE_PREPROCESS_COMMAND:append:summit-secure = "do_machine_id; "
