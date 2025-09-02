SUMMARY = "Summit SOM Basic Components"
SECTION = "net/misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

RDEPENDS:${PN} = " \
    kernel-modules \
    ca-certificates \
    tzdata-core \
    tzdata-posix \
    iproute2 \
    chrony \
    chronyc \
    gptfdisk \
    optee-client \
    libgpiod \
    libgpiod-tools \
    summit-automount \
    summit-initdata \
    summit-update \
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbgadget', 'summit-usbgadget', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'alsa', 'alsa-utils-alsamixer alsa-utils-aplay', '', d)} \
    "

RDEPENDS:${PN}:append:imx8mp-summitsom = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'packagegroup-radio-stack-60', '', d)} \
    "
