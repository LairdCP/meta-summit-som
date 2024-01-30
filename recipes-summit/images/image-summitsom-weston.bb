DESCRIPTION = "Summit SOM Weston Image"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

IMAGE_FEATURES += "splash hwcodecs weston"

CORE_IMAGE_EXTRA_INSTALL += "\
	${IMAGE_INSTALL_BASIC} \
	${IMAGE_INSTALL_DIAG} \
	summit-mcu-demos \
	packagegroup-fsl-gstreamer1.0 \
	"

IMAGE_BOOT_FILES += "fitImageMcu.bin"
WKS_FILE_DEPENDS += "summit-mcu-demos"
