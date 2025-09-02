DESCRIPTION = "Arducam Pivariety Driver"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

inherit module

SRC_URI = " \
        file://arducam-pivariety \
        "

# The kernel module MUST NOT be built in WORKDIR, otherwise clean will break it
S = "${WORKDIR}/arducam-pivariety"
