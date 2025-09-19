# This image extends meta-b2qt-embedded-qbsp with additional Summit packages

require recipes-qt/meta/meta-b2qt-embedded-qbsp.bb

QBSP_SDK = "${DISTRO}-${SDKMACHINE}-${QBSP_SDK_TASK}-${MACHINE}"
QBSP_IMAGE_TASK = "image-summitsom-b2qt"
