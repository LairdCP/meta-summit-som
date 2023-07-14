# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "A collection of framework independent HTTP protocol utils."
HOMEPAGE = "https://github.com/MagicStack/httptools"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0a2d82955bf3facdf04cb882655e840e \
                    file://vendor/http-parser/LICENSE-MIT;md5=9bfa835d048c194ab30487af8d7b3778 \
                    file://vendor/llhttp/LICENSE-MIT;md5=f5e274d60596dd59be0a1d1b19af7978"

SRC_URI[sha256sum] = "9fc6e409ad38cbd68b177cd5158fc4042c796b82ca88d99ec78f07bed6c6b796"

inherit setuptools3 pypi

DEPENDS += "${PYTHON_PN}-cython-native"
