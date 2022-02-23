FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://ifconfig.cfg \
        file://fdisk.cfg \
        "

RRECOMMENDS_${PN}_remove = "${PN}-udhcpc"