RDEPENDS:packagegroup-basic:remove:summitsom = "avahi-daemon avahi-utils"
TASK_BASIC_SSHDAEMON:summitsom = "dropbear"

RRECOMMENDS:packagegroup-basic:remove:summitsom = " \
    kernel-module-g-ether kernel-module-g-serial kernel-module-g-mass-storage \
    "
