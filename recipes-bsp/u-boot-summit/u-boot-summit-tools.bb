SUMMARY = "Summit U-Boot Tools for Ezurio boards"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
SECTION = "bootloaders"

require recipes-bsp/u-boot/u-boot-tools.inc

inherit pkgconfig summit-platform-version

SRC_URI = "${SUMMIT_EXTERNAL_GIT_URI}/u-boot-som.git;${SUMMIT_EXTERNAL_GIT_SUFFIX}"
SRC_URI:summit-internal = "${SUMMIT_INTERNAL_GIT_URI}/cp_linux-u-boot-som60.git;${SUMMIT_INTERNAL_GIT_SUFFIX}"

S = "${UNPACKDIR}/git"
B = "${UNPACKDIR}/build"

DEPENDS += "flex-native bison-native python3-setuptools-native" 

PROVIDES:class-target += "u-boot-tools"
PROVIDES:class-native += "u-boot-tools-native"
PROVIDES:class-nativesdk += "nativesdk-u-boot-tools"
