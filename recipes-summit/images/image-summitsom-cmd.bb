DESCRIPTION = "Summit SOM Command Line Image"

require image-summitsom-gen.inc

SWUPDATE_IMAGES_FSTYPES[image-summitsom-cmd] = ".squashfs-zstd.verity"

CORE_IMAGE_EXTRA_INSTALL += "\
	${IMAGE_INSTALL_BASIC} \
	${IMAGE_INSTALL_DIAG} \
	linuxptp \
	packagegroup-fsl-tools-audio \
	mpg123 \
	read-edid \
	fbida \
	systemd-analyze \
	bluez5-testtools \
	mc-mint \
	summit-m7-demos \
	weblcm-python \
	v4l-utils \
	"

IMAGE_BOOT_FILES += "fitImageMcu.bin"
WKS_FILE_DEPENDS += "fitImageMcu.bin"
