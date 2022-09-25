OPENSSLVER = "_openssl_3_0"

RDEPENDS:${PN} += "libcrypto (>= 3.0)"

require adaptive-ww.inc radio-stack-som-version.inc
