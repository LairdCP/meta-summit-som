OPENSSLVER = ""

RDEPENDS_${PN} += "libssl (>= 1.1.0) libcrypto (>= 1.1.0)"

require summit-supplicant.inc radio-stack-som-version.inc

SRC_URI += "file://0001-wpa-cli-nm.patch"
