DESCRIPTION = "Summit SOM Radio Manufacturing Image"

require image-summitsom-gen.inc

SWUPDATE_IMAGES_FSTYPES[image-summitsom-mfg] = ".squashfs-zstd.verity"

IMAGE_INSTALL += "\
	${IMAGE_INSTALL_BASIC} \
	${IMAGE_INSTALL_DIAG} \
	linuxptp \
	packagegroup-fsl-tools-audio \
	mpg123 \
	read-edid \
	fbida \
	python3 \
	"

# Radio manufacturing components
IMAGE_INSTALL += "\
	packagegroup-radio-stack-mfg \
	packagegroup-radio-stack-mfgbridge-8997 \
	mfg60n-lrt-som \
	sterling60-firmware-som \
	echotest \
	"