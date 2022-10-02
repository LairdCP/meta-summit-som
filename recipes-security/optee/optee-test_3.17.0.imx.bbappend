do_install () {
	oe_runmake install DESTDIR=${D}${root_prefix}
	if [ -n ${root_prefix} ]; then
		mv ${D}${root_prefix}${libdir}/tee-supplicant ${D}${libdir}
		rm -r ${D}${root_prefix}${prefix}
	fi

}
