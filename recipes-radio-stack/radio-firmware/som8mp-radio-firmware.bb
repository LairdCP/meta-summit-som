SUMMARY = "Laird Connectivity Summit 60 Firmware for SOM"

require radio-firmware-60.inc radio-stack-som-version.inc

SRC_URI = " \
        ${LRD_URI}/laird-som8mp-radio-firmware-${PV}.tar.bz2;name=som8mp-radio-firmware \
        "
