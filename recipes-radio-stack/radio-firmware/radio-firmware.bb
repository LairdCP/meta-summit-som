SUMMARY = "Laird Connectivity Summit 60 Firmware"

LRD_URI = "https://files.devops.rfpros.com/builds/linux/firmware/${PV}"

SRC_URI = " \
        ${LRD_URI}/laird-summit60-firmware-sdio-sdio-${PV}.tar.bz2 \
        ${LRD_URI}/laird-summit60-firmware-sdio-uart-${PV}.tar.bz2 \
        ${LRD_URI}/laird-summit60-firmware-pcie-uart-${PV}.tar.bz2 \
        "

include radio-firmware.inc
