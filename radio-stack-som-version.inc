BB_STRICT_CHECKSUM:summit-internal = "ignore"

PREMIRRORS:summit-internal = ""
MIRRORS:summit-internal = ""

PV = "${RADIO_STACK_VERSION_SOM}"

SUMMIT_URI_LOCAL = "file://${TOPDIR}/../release"
SUMMIT_URI_BASE = "https://github.com/LairdCP/wb-package-archive/releases/download/LRD-REL-${PV}"
SUMMIT_URI_BASE:summit-internal = "https://files.devops.rfpros.com/builds/linux"

require radio-stack-som8mp-version-base.inc radio-stack-som8mp-hashes.inc
