#
# Copyright 2021 Laird Connectivity
#
# SPDX-License-Identifier:      GPL-2.0+
#

obj-y += imx8mp_summitsom.o

ifdef CONFIG_SPL_BUILD
obj-y += spl.o

ifdef CONFIG_SYS_SUMMITSOM_2G
obj-y += lpddr4_timing_2gb.o
endif
ifdef CONFIG_SYS_SUMMITSOM_1G
obj-y += lpddr4_timing_1gb.o
endif
ifdef CONFIG_SYS_SUMMITSOM_512M
obj-y += lpddr4_timing_512mb.o
endif

endif

obj-y += ../../freescale/common/
