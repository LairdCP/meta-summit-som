# This image extends b2qt-embedded-qt6-image with additional Ezurio packages

require recipes-qt/images/b2qt-embedded-qt6-image.bb
require recipes-summit/images/image-summitsom-cmd.bb

IMAGE_FEATURES:remove = "tools-profile"
