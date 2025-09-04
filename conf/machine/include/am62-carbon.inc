require conf/machine/include/k3.inc
require conf/machine/include/mc_k3r5.inc
require conf/machine/include/carbon-summitsom.inc

SOC_FAMILY:append = ":am62xx"

MACHINE_FEATURES += "screen gpu rtc"

require conf/machine/include/mesa-pvr.inc
PREFERRED_PROVIDER_virtual/gpudriver ?= "${BSP_ROGUE_DRIVER_PROVIDER}"

TFA_BOARD = "lite"
TFA_K3_SYSTEM_SUSPEND = "1"

OPTEEMACHINE = "k3-am62x"

SERIAL_CONSOLES = "115200;ttyS2"
