# fitImage kernel compression algorithm
FIT_KERNEL_COMP_ALG ?= "gzip"
FIT_KERNEL_COMP_ALG_EXTENSION ?= ".gz"

DEPENDS += "${@bb.utils.contains("FIT_KERNEL_COMP_ALG", "zstd", "zstd-native", "", d)}"

uboot_prep_kimage() {
	if [ -e arch/${ARCH}/boot/compressed/vmlinux ]; then
		vmlinux_path="arch/${ARCH}/boot/compressed/vmlinux"
		linux_suffix=""
		linux_comp="none"
	elif [ -e arch/${ARCH}/boot/vmlinuz.bin ]; then
		rm -f linux.bin
		cp -l arch/${ARCH}/boot/vmlinuz.bin linux.bin
		vmlinux_path=""
		linux_suffix=""
		linux_comp="none"
	else
		vmlinux_path="vmlinux"
		linux_suffix="${FIT_KERNEL_COMP_ALG_EXTENSION}"
		linux_comp="${FIT_KERNEL_COMP_ALG}"
	fi

	[ -n "${vmlinux_path}" ] && ${OBJCOPY} -O binary -R .note -R .comment -S "${vmlinux_path}" linux.bin

	if [ "${linux_comp}" != "none" ] ; then
		if [ "${linux_comp}" = "gzip" ] ; then
			gzip -9 linux.bin
		elif [ "${linux_comp}" = "lzo" ] ; then
			lzop -9 linux.bin
		elif [ "${linux_comp}" = "zstd" ] ; then
			zstd -19 linux.bin -o linux.bin.zstd
		fi
		mv -f "linux.bin${linux_suffix}" linux.bin
	fi

	echo "${linux_comp}"
}