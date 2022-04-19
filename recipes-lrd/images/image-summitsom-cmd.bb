DESCRIPTION = "Summit SOM Command Line Image"

require image-summitsom-gen.inc

SWUPDATE_IMAGES_FSTYPES[image-summitsom-cmd] = ".squashfs-zstd.verity"

IMAGE_INSTALL += "\
	${IMAGE_INSTALL_BASIC} \
	${IMAGE_INSTALL_DIAG} \
	linuxptp \
	packagegroup-fsl-tools-audio \
	mpg123 \
	read-edid \
	fbida \
	python3 \
	systemd-analyze \
	bluez5-testtools \
	python-dbus \
	mc-mint \
	"
