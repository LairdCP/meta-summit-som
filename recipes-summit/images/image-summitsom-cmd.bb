DESCRIPTION = "Summit SOM Command Line Image"

inherit image-summitsom-gen image-summitsom-sd-gen image-summitsom-swu-gen

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
	summit-mcu-demos \
	summit-rcm \
	summit-rcm-awm-plugin \
	summit-rcm-chrony-plugin \
	summit-rcm-firewall-plugin \
	${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'summit-rcm-bluetooth-plugin', '', d)} \
	v4l-utils \
	qfirehose \
	"

IMAGE_BOOT_FILES += "fitImageMcu.bin"
WKS_FILE_DEPENDS += "summit-mcu-demos"
