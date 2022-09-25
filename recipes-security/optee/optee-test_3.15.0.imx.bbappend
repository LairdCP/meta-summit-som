do_install () {
	install -D -m 755 ${B}/xtest/xtest ${D}${bindir}/xtest

	install -d ${D}${nonarch_base_libdir}/optee_armtz
	find ${B}/ta -name '*.ta' | while read name; do
		install -m 444 $name ${D}${nonarch_base_libdir}/optee_armtz/
	done

	install -d ${D}${nonarch_libdir}/tee-supplicant/plugins/
	install ${B}/supp_plugin/*plugin ${D}/${nonarch_libdir}/tee-supplicant/plugins/
}

FILES:${PN} = "${bindir} ${nonarch_base_libdir}/optee_armtz/ ${nonarch_libdir}/tee-supplicant/plugins/"