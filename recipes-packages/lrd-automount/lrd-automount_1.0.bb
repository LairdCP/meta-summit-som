SUMMARY = "Laird Connectivity Auto Mount"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://90-usbmount.rules \
    file://90-usbmount-sysv.rules \
    file://91-mmcmount.rules \
    file://91-mmcmount-sysv.rules \
    file://usb-mount.sh \
    file://usb-mount@.service \
	"

PACKAGECONFIG ??= "usbmount"

MMC_USER ?= ""
USB_USER ?= ""

SYSTEMD_SERVICE_${PN} = "usb-mount@.service"
SYSTEMD_AUTO_ENABLE = "disable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

S = "${WORKDIR}"

FILES_${PN} += "${systemd_unitdir}/system/* ${sysconfdir}/udev/rules.d/*"

do_install() {
	install -D -m 0755 ${S}/usb-mount.sh \
		${D}${bindir}/usb-mount.sh

	install -d ${D}${sysconfdir}/default
	echo "MOUNT_USER_MMC=${MMC_USER}" \
		>${D}${sysconfdir}/default/usb-mount
	echo "MOUNT_USER_USB=${USB_USER}" \
		>>${D}${sysconfdir}/default/usb-mount

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 644 ${S}/usb-mount@.service \
            ${D}${systemd_unitdir}/system/usb-mount@.service

        if ${@bb.utils.contains('PACKAGECONFIG','usbmount','true','false',d)}; then
            install -D -m 644 ${S}/90-usbmount-sysv.rules \
                ${D}${sysconfdir}/udev/rules.d/90-usbmount.rules
        fi

        if ${@bb.utils.contains('PACKAGECONFIG','mmcmount','true','false',d)}; then
            install -D -m 644 ${S}/91-mmcmount-sysv.rules \
                ${D}${sysconfdir}/udev/rules.d/91-mmcmount.rules
        fi
    else
        if ${@bb.utils.contains('PACKAGECONFIG','usbmount','true','false',d)}; then
            install -D -m 644 ${S}/90-usbmount.rules \
                ${D}${sysconfdir}/udev/rules.d/90-usbmount.rules
        fi

        if ${@bb.utils.contains('PACKAGECONFIG','mmcmount','true','false',d)}; then
            install -D -m 644 ${S}/91-mmcmount.rules \
                ${D}${sysconfdir}/udev/rules.d/91-mmcmount.rules
        fi
    fi
}