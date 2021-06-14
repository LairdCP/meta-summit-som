SUMMARY = "Laird Connectivity Wi-Fi Backports for Summit 60"

BACKPORTS_CONFIG = "defconfig-summit60"

LRD_URI = "https://files.devops.rfpros.com/builds/linux/backports/laird/${PV}"

SRC_URI += "${LRD_URI}/${BACKPORTS_FILE}"

include backports-laird.inc
