SUMMARY = "Summit Image Tools"

LICENSE = "Ezurio-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.ezurio;md5=fd3dd0630b215465b6f50540642d5b93"

SRC_URI = " \
    file://LICENSE.ezurio \
    file://mksdcard.sh \
    file://imx-rescue.uuu \
    file://imx8mm-rescue.uuu \
    "

S = "${UNPACKDIR}"

do_install () {
    install -D -m 0644 -t "${DEPLOY_DIR_IMAGE}" "${S}/mksdcard.sh"
}

do_install:imx-generic-bsp:summitsom-rescue-initramfs () {
    install -D -m 0644 -t "${DEPLOY_DIR_IMAGE}" "${S}/imx-rescue.uuu"
}

do_install:mx8mm-generic-bsp:summitsom-rescue-initramfs () {
    install -D -m 0644 "${S}/imx-rescue.uuu" "${DEPLOY_DIR_IMAGE}/imx-rescue.uuu"
}
