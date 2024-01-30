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
	summit-rcm \
	summit-rcm-awm-plugin \
	summit-rcm-chrony-plugin \
	summit-rcm-firewall-plugin \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'summit-rcm-bluetooth-plugin', '', d)} \
	v4l-utils \
	qfirehose \
	packagegroup-fsl-gstreamer1.0 \
	packagegroup-core-buildessential \
	"
