SUMMARY = "Laird Connectivity Web Configuration Utility"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit setuptools3

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/LairdCP/weblcm-python.git;protocol=https;branch=master"
SRC_URI_laird-internal = "git://git@git.devops.rfpros.com/cp_apps/weblcm-python.git;protocol=ssh;branch=master"

SRCREV = "${AUTOREV}"

RDEPENDS_${PN} = "\
        python3 \
        python3-core \
        python3-io \
        python3-pygobject \
        python3-systemd \
        python3-networkmanager \
        python3-cherrypy \
        "