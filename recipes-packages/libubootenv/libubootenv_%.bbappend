FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-crc32.patch"

DEPENDS_remove = "zlib"
