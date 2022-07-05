SUMMARY = "setuptools_scm plugin for git archives"
HOMEPAGE = "https://pypi.org/project/setuptools-scm-git-archive/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI[sha256sum] = "b048b27b32e1e76ec865b0caa4bb85df6ddbf4697d6909f567ac36709f6ef2f0"

PYPI_PACKAGE = "setuptools_scm_git_archive"

inherit pypi setuptools3

DEPENDS += "python3-setuptools-scm-native"

BBCLASSEXTEND = "native nativesdk"
