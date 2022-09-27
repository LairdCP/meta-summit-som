DESCRIPTION = "MINT - Multicast Packet Generator"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/gpl.txt;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "http://downloads.sourceforge.net/project/mc-mint/mc-mint/Mint%201.2/mint-${PV}.tar.gz \
           file://0001-mc-mint-regular_xmit.patch \
           file://0002-mc-mint-makefile.patch \
           file://0003-mc-mint-in6_addr.patch \
           file://0004-mc-mint-logger.patch \
           file://0005-mc-mint-options.patch \
           file://0006-mc-mint-usage.patch \
           file://0007-mc-mint-hostname.patch \
           file://0008-mc-mint-socketlen.patch \
           "

SRC_URI[md5sum] = "75d513fff571d283c407608c4bcbe2c5"
SRC_URI[sha256sum] = "470fc10c7060e002f71c2bc513be428dfe0dd01529e8f1edcd650c1e6d90d773"

S = "${WORKDIR}/mint-${PV}"

do_install() {
    install -D -m 755 mint ${D}${bindir}/mint
}
