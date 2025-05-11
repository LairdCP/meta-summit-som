SUMMARY = "swupdate IPC client Python module"

LICENSE = "Ezurio"
NO_GENERIC_LICENSE[Ezurio] = "LICENSE.ezurio"
LIC_FILES_CHKSUM = "\
    file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93 \
    "

inherit setuptools3 summit-platform-version

DEPENDS += "swupdate"

SRC_URI = "git://github.com/Ezurio/lrd-userspace-examples.git;protocol=https;${SUMMIT_PLATFORM_BRANCH}"
SRC_URI:summit-internal = "git://git@github.com/rfpros/cp_linux-lrd-userspace-examples.git;protocol=ssh;${SUMMIT_PLATFORM_BRANCH}"

S = "${WORKDIR}/git/swclient"

RDEPENDS:${PN} = "python3 swupdate"
