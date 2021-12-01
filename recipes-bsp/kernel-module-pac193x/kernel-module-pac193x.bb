DESCRIPTION = "Microchip PCA193x Driver"

LICENSE = "GPL-2.0+ | LGPL-2.1+"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c \
    file://${COREBASE}/meta/files/common-licenses/LGPL-2.1-or-later;md5=2a4f4fd2128ea2f65047ee63fbca9f68 \
"

inherit module

SRC_URI = " \
        file://Makefile \
        file://pac193x.c \
        "

S = "${WORKDIR}"
