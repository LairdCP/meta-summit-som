do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	for subdir in dwe isp video; do
		oe_runmake -C ${subdir} \
			KERNEL_PATH=${STAGING_KERNEL_DIR}   \
			KERNEL_VERSION=${KERNEL_VERSION}    \
			CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
			AR="${KERNEL_AR}" OBJCOPY="${KERNEL_OBJCOPY}" \
			STRIP="${KERNEL_STRIP}" \
			O=${STAGING_KERNEL_BUILDDIR} \
			KBUILD_EXTRA_SYMBOLS="${KBUILD_EXTRA_SYMBOLS}"
	done
}

do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	for subdir in dwe isp video; do
		oe_runmake -C ${subdir} DEPMOD=echo \
			MODLIB="${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}" \
			INSTALL_FW_PATH="${D}${nonarch_base_libdir}/firmware" \
			CC="${KERNEL_CC}" LD="${KERNEL_LD}" OBJCOPY="${KERNEL_OBJCOPY}" \
			STRIP="${KERNEL_STRIP}" \
			O=${STAGING_KERNEL_BUILDDIR} \
			KBUILD_EXTRA_SYMBOLS="${KBUILD_EXTRA_SYMBOLS}" \
			modules_install
	done

	install -Dm0644 "${B}/${MODULES_MODULE_SYMVERS_LOCATION}/Module.symvers" \
		${D}${includedir}/${BPN}/Module.symvers
	sed -e 's:${B}/::g' -i ${D}${includedir}/${BPN}/Module.symvers
}
