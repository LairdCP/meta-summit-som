FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://git \
	file://0001-bsp-integ.patch \
	file://0003-ddr-1866MHz.patch \
	file://0004-fs-fat-fix-file_fat_detectfs.patch \
	file://0005-fs-fat-add-file-attributes-to-struct-fs_dirent.patch \
	file://0006-fs-fat-check-for-buffer-size-before-reading-blocks.patch \
	file://0007-crypto-fsl-fsl_hash-Fix-dcache-issue-in-caam_hash_fi.patch \
	file://0008-crypto-fsl_hash-Remove-unnecessary-alignment-check-i.patch \
	file://0009-required-key-for-all.patch \
	"
