SUMMARY = "Laird Connectivity Summit 60 Firmware for SOM"

require radio-firmware-60.inc radio-stack-som-version.inc

SRC_URI = " \
        ${LRD_URI}/laird-summit60-firmware-sdio-sdio-${PV}.tar.bz2;name=summit60-firmware-sdio-sdio \
        ${LRD_URI}/laird-summit60-firmware-sdio-uart-${PV}.tar.bz2;name=summit60-firmware-sdio-uart \
        ${LRD_URI}/laird-summit60-firmware-pcie-uart-${PV}.tar.bz2;name=summit60-firmware-pcie-uart \
        "
