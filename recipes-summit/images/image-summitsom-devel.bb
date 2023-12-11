DESCRIPTION = "Summit SOM Devel Image"

inherit image-summitsom-gen image-summitsom-sd-gen

# Data partition size
WIC_ROOTFS_DATA_FIXED_SIZE = "5G"

CORE_IMAGE_EXTRA_INSTALL += "\
	${IMAGE_INSTALL_BASIC} \
	${IMAGE_INSTALL_DIAG} \
	linuxptp \
	packagegroup-fsl-tools-audio \
	read-edid \
	systemd-analyze \
	v4l-utils \
	qfirehose \
	packagegroup-core-buildessential \
	"
