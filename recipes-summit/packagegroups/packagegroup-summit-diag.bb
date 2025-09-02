SUMMARY = "Summit SOM Diagnostic and debugging tools"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    iperf2 \
    iperf3 \
    htop \
    tcpdump \
    can-utils \
    stress-ng \
    mc-mint \
    i2c-tools \
    spitools \
    mdio-tools \
    libinput \
    evtest \
    linuxptp \
    mpg123 \
    fbida \
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', 'usbutils', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pci', 'pciutils', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'alsa', 'alsa-utils-speakertest', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-analyze', '', d)} \
    "
