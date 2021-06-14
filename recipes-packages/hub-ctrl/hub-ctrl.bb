DESCRIPTION = "USB Hub Control"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

DEPENDS = "libusb"

SRC_URI = "file://libpt2 file://examples"

inherit pkgconfig

S = "${WORKDIR}"

do_compile () {
    oe_runmake -C libpt2
    oe_runmake -C examples/register_rw
} 

do_install () {
    install -D -m 755 ${S}/examples/register_rw/out/register_rw  ${D}${bindir}/register_rw
}