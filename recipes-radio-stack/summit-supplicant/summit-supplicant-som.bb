OPENSSLVER = "_openssl_3_0"

RDEPENDS:${PN} += "libssl (>= 3.0) libcrypto (>= 3.0)"

SRC_URI += "file://0001-wpa-cli-nm.patch"

require summit-supplicant.inc radio-stack-som-version.inc
