SUMMARY = "Summit SOM USB Gadget"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch systemd

SRC_URI = " \
    file://LICENSE.ezurio \
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
VENDOR_ID ?= "0x1fa3"
PRODUCT_ID ?= "0x0002"

S = "${WORKDIR}"

FILES:${PN} += "${systemd_system_unitdir} ${libdir}"

do_install() {
    install -D -m 0755 "${S}/usb-gadget.sh" \
        "${D}${bindir}/usb-gadget.sh"
    install -D -m 0600 "${S}/shared-usb0.nmconnection" \
        "${D}${libdir}/NetworkManager/system-connections/shared-usb0.nmconnection"

    install -d "${D}${sysconfdir}/default"
    {
        echo "USB_GADGET_ETHER_PORTS=${ETHERNET_PORTS}"
        echo "USB_GADGET_ETHER=${GADGET_TYPE}"
        echo "USB_GADGET_ETHER_LOCAL_MAC=\"${LOCAL_MAC}\""
        echo "USB_GADGET_ETHER_REMOTE_MAC=\"${REMOTE_MAC}\""
        echo "USB_GADGET_SERIAL_PORTS=${SERIAL_PORTS}"
        echo "USB_GADGET_VENDOR_ID=\"${VENDOR_ID}\""
        echo "USB_GADGET_PRODUCT_ID=\"${PRODUCT_ID}\""
    } > "${D}${sysconfdir}/default/usb-gadget"

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -D -m 0644 "${S}/usb-gadget.service" \
            "${D}${systemd_system_unitdir}/usb-gadget.service"
    else
        install -D -m 0644 "${S}/usb-gadget.rules" \
            "${D}${libdir}/udev/rules.d/usb-gadget.rules"
    fi
}
