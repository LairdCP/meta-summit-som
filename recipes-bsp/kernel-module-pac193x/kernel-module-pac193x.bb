DESCRIPTION = "Microchip PCA193x Driver"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

inherit module

SRC_URI = " \
        file://Makefile \
        file://pac193x.c \
        "

S = "${WORKDIR}"
