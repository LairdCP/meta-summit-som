SUMMARY = "Summit Backports for SOM"

BACKPORTS_CONFIG = " \
	${@bb.utils.contains('DISTRO_FEATURES','bluetooth','defconfig-som8mplus','defconfig-som8mplus_nbt',d)} \
	"

RCONFLICTS:${PN} = " \
        kernel-module-summit-backports-summit \
        kernel-module-sterling-backports-summit \
        kernel-module-lwb-backports-summit \
        kernel-module-lwb5p-backports-summit \
        "

module_conf_lrdmwl = "options lrdmwl null_scan_count=1"

KERNEL_MODULE_PROBECONF += "lrdmwl"

require summit-backports.inc radio-stack-som-version.inc
