FILESEXTRAPATHS:prepend:imx8mp-summitsom := "${THISDIR}/files/mcu:"

IMAGE_INSTALL:append:imx8mp-summitsom = " summit-mcu-demos"
IMAGE_BOOT_FILES:append:imx8mp-summitsom = " fitImageMcu.bin"
SWUPDATE_IMAGES:append:imx8mp-summitsom = " fitImageMcu.bin"
WKS_FILE_DEPENDS:append:imx8mp-summitsom = " summit-mcu-demos"
