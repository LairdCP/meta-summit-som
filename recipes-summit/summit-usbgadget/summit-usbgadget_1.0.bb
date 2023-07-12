SUMMARY = "Summit SOM USB Gadget"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://usb-gadget.sh \
    file://usb-gadget.rules \
    file://usb-gadget.service \
    file://shared-usb0.nmconnection \
	"

SYSTEMD_SERVICE:${PN} = "usb-gadget.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

ALLOW_EMPTY:${PN}-dev = "0"
ALLOW_EMPTY:${PN}-dbg = "0"

ETHERNET_PORTS ?= "1"
GADGET_TYPE ?= "ncm"
LOCAL_MAC ?= "DE:AD:BE:EF:00:00"
REMOTE_MAC ?= "DE:AD:BE:EF:00:01"
SERIAL_PORTS ?= "0"

S = "${WORKDIR}"

FILES:${PN} += "${systemd_system_unitdir} ${libdir}"

do_install() {
    install -D -m 0755 ${S}/usb-gadget.sh \
        ${D}${bindir}/usb-gadget.sh
    install -D -m 0600 shared-usb0.nmconnection \
        ${D}${libdir}/NetworkManager/system-connections/shared-usb0.nmconnection

    install -d ${D}${sysconfdir}/default
    echo "USB_GADGET_ETHER_PORTS=${ETHERNET_PORTS}"       > ${D}${sysconfdir}/default/usb-gadget
    echo "USB_GADGET_ETHER=${GADGET_TYPE}"               >> ${D}${sysconfdir}/default/usb-gadget
    echo "USB_GADGET_ETHER_LOCAL_MAC=\"${LOCAL_MAC}\""   >> ${D}${sysconfdir}/default/usb-gadget
    echo "USB_GADGET_ETHER_REMOTE_MAC=\"${REMOTE_MAC}\"" >> ${D}${sysconfdir}/default/usb-gadget
    echo "USB_GADGET_SERIAL_PORTS=${SERIAL_PORTS}"       >> ${D}${sysconfdir}/default/usb-gadget

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 0644 ${S}/usb-gadget.service \
            ${D}${systemd_system_unitdir}/usb-gadget.service
    else
        install -D -m 0644 ${WORKDIR}/usb-gadget.rules \
            ${D}${libdir}/udev/rules.d/usb-gadget.rules
    fi
}