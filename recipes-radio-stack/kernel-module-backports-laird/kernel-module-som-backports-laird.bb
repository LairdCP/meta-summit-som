SUMMARY = "Laird Connectivity Backports for SOM"

BACKPORTS_CONFIG = " \
	${@bb.utils.contains('DISTRO_FEATURES','bluetooth','defconfig-som8mplus','defconfig-som8mplus_nbt',d)} \
	"

RCONFLICTS_${PN} = " \
        kernel-module-summit-backports-laird \
        kernel-module-sterling-backports-laird \
        kernel-module-lwb-backports-laird \
        kernel-module-lwb5p-backports-laird \
        "

require backports-laird.inc radio-stack-som-version.inc
