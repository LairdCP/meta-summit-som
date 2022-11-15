BB_STRICT_CHECKSUM:summit-internal = "ignore"

PREMIRRORS:summit-internal = ""
MIRRORS:summit-internal = ""

PV = "${RADIO_STACK_VERSION_SOM}"

LRD_URI_LOCAL = "file://${TOPDIR}/../release"
LRD_URI_BASE = "https://github.com/LairdCP/wb-package-archive/releases/download/LRD-REL-${PV}"

require radio-stack-som8mp-version-base.inc radio-stack-som8mp-hashes.inc
