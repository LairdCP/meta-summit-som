SUMMARY = "Laird Connectivity Auto Mount"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = " \
    file://90-usbmount.rules \
    file://91-mmcmount.rules \
    file://usb-mount.sh \
 	"

MMC_USER ?= ""
USB_USER ?= ""

do_configure[noexec] = "1"
do_compile[noexec] = "1"

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

S = "${WORKDIR}"

FILES_${PN} += "${sysconfdir}/udev/rules.d ${sysconfdir}/default"

do_install() {
	install -D -m 0755 -t ${D}${bindir} ${S}/usb-mount.sh
	install -D -m 0644 -t ${D}${sysconfdir}/udev/rules.d \
		${S}/90-usbmount.rules ${S}/91-mmcmount.rules

	install -d ${D}${sysconfdir}/default
	echo "MOUNT_USER_MMC=${MMC_USER}" >>${D}${sysconfdir}/default/usb-mount
	echo "MOUNT_USER_USB=${USB_USER}" >>${D}${sysconfdir}/default/usb-mount
}