SUMMARY = "Signature Provider plugin for SPSDK using PKCS#11 interface"
DESCRIPTION = "NXP SPSDK plugin that provides PKCS#11-based signing via the \
standard SignatureProvider entry point interface."
HOMEPAGE = "https://github.com/nxp-mcuxpresso/spsdk_plugins/tree/master/pkcs11"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fe2a425fb1f291c670f58b9e3771878e"

PYPI_PACKAGE = "spsdk_pkcs11"

SRC_URI[sha256sum] = "ceb4249e652b10da1dd6ed2faee137961d1a6d69c01ea52dc064e1647ee71279"

RDEPENDS:${PN} += " \
    python3-pkcs11-native \
    python3-spsdk-native \
    "

inherit pypi python_setuptools_build_meta native
