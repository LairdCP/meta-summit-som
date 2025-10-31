EXTRA_OECMAKE:append:summitsom = " \
    -DCFG_TEE_FS_PARENT_PATH='/perm/tee' \
    "

inherit ${@oe.utils.conditional('VIRTUAL-RUNTIME_dev_manager', 'busybox-mdev', '', 'useradd', d)}
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system teepriv; --system tee"
