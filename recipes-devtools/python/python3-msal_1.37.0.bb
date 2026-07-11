DESCRIPTION = "Microsoft Authentication Library (MSAL) for Python makes it easy to authenticate to Microsoft Entra ID."
HOMEPAGE = "https://github.com/AzureAD/microsoft-authentication-library-for-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bbfbc44677c93751d972e8b36751a695"


SRC_URI[sha256sum] = "1b1672a33ee467c1d70b341bb16cafd51bb3c817147a95b93263794b03971bec"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-requests \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-pyjwt \
    "

BBCLASSEXTEND = "native nativesdk"
