SUMMARY = "Summit Image Tools"

LICENSE = "Ezurio-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

inherit native

SRC_URI = " \
    file://LICENSE.ezurio \
    file://mksdcard.sh \
    "

S = "${UNPACKDIR}"

do_install () {
    install -D -m 0755 -t "${D}${bindir}" "${S}/mksdcard.sh"
}
