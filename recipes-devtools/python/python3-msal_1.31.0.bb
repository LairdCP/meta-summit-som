DESCRIPTION = "Microsoft Authentication Library (MSAL) for Python makes it easy to authenticate to Microsoft Entra ID."
HOMEPAGE = "https://github.com/AzureAD/microsoft-authentication-library-for-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bbfbc44677c93751d972e8b36751a695"


SRC_URI[sha256sum] = "2c4f189cf9cc8f00c80045f66d39b7c0f3ed45873fd3d1f2af9f22db2e12ff4b"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-requests \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-pyjwt \
    "

BBCLASSEXTEND = "native nativesdk"
