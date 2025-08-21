SUMMARY = "Summit Set Mode"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit allarch

SRC_URI = " \
    file://LICENSE.ezurio;subdir=src \
    file://set-mode;subdir=src \
    "

S = "${WORKDIR}/src"

RDEPENDS:${PN} = "\
    libubootenv-bin \
    u-boot-tools-mkimage \
    "

do_install () {
    install -D -m 0755 "${S}"/set-mode "${D}"/usr/bin/set-mode
}
