FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://ifconfig.cfg \
        file://fdisk.cfg \
        file://ps.cfg \
        "

RRECOMMENDS_${PN}:remove = "${PN}-udhcpc"