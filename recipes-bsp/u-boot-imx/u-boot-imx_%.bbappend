FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://git \
	file://0001-bsp-integ.patch \
	file://0003-ddr-1866MHz.patch \
	file://0004-fs-fat-fix-file_fat_detectfs.patch \
	file://0005-fs-fat-add-file-attributes-to-struct-fs_dirent.patch \
	file://0006-fs-fat-check-for-buffer-size-before-reading-blocks.patch \
	"
