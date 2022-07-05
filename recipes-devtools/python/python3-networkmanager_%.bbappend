FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://0001-update-to-1.20.4.patch \
        file://0002-nm-1-36.patch \
"

RDEPENDS_${PN}_remove = "networkmanager"
RDEPENDS_${PN} += "lrd-networkmanager-som"