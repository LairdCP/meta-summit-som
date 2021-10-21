DEPENDS_remove = " vala-native ${VALADEPENDS}"

EXTRA_OECONF += "--disable-vala"

do_patch_append() {
    bb.build.exec_func('do_cp_vapigen', d)
}

do_cp_vapigen () {
    cp ${S}/m4/vapigen.m4 ${S}/vapigen.m4
}

do_configure_prepend() {
    cp ${S}/vapigen.m4 ${S}/m4/vapigen.m4
}