DESCRIPTION = "Texas Instruments TAC5x1x Driver"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = " \
        file://tac5x1x \
        "

# The kernel module MUST NOT be built in UNPACKDIR, otherwise clean will break it
S = "${UNPACKDIR}/tac5x1x"
