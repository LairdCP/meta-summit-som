FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://ifconfig.cfg \
        file://fdisk.cfg \
        file://ps.cfg \
        "

RRECOMMENDS:${PN}_remove_summitsom = "${PN}-udhcpc"
