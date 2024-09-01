EXTRA_OECMAKE:append:summitsom = " -DCFG_TEE_FS_PARENT_PATH='/perm/tee'"

do_install:append() {
    chmod 0644 ${D}${nonarch_base_libdir}/udev/rules.d/tee-udev.rules
}
