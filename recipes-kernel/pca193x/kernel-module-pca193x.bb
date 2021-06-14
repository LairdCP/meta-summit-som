DESCRIPTION = "Microchip PCA193x Driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = " \
        file://Makefile \
        file://pac193x.c \
        "

S = "${WORKDIR}"

EXTRA_OEMAKE += "KSRC='${KERNEL_SRC}'"
