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
	systemd-analyze \
	bluez5-testtools \
	mc-mint \
	lrd-m7-demos \
	weblcm-python \
	"

IMAGE_BOOT_FILES += "lrd-m7-low-power-wakeup-demo-itcm.bin"
SWUPDATE_IMAGES += "lrd-m7-low-power-wakeup-demo-itcm.bin"
