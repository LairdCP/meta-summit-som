FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://ifconfig.cfg \
        file://fdisk.cfg \
        file://ps.cfg \
        "

RRECOMMENDS:${PN}:remove:summitsom = "${PN}-udhcpc"
