# This image extends b2qt-embedded-qt6-image with additional Ezurio packages

FILESEXTRAPATHS:append := "\
${TOPDIR}/../sources/meta-summit-som/recipes-summit/images/files/nomcu:\
${TOPDIR}/../sources/meta-summit-som/recipes-summit/images/files:\
"

require recipes-qt/images/b2qt-embedded-qt6-image.bb
require recipes-summit/images/image-summitsom-cmd.bb

IMAGE_FEATURES:remove = "tools-profile"
