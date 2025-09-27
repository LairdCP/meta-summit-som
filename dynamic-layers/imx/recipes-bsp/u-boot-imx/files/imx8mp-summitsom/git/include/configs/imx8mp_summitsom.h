/* SPDX-License-Identifier: GPL-2.0+ */
/*
 * Copyright 2019 NXP
 */

#ifndef __IMX8MP_SUMMITSOM_H
#define __IMX8MP_SUMMITSOM_H

#include <linux/sizes.h>
#include <linux/stringify.h>
#include <asm/arch/imx-regs.h>

#ifdef CONFIG_ENV_WRITEABLE_LIST
#define CFG_ENV_FLAGS_LIST_STATIC "conf:sw,version:sw"
#endif

/* Link Definitions */

#define CFG_SYS_INIT_RAM_ADDR           0x40000000
#define CFG_SYS_INIT_RAM_SIZE           0x80000

/* Totally 2GB DDR */
#define CFG_SYS_SDRAM_BASE              0x40000000

#define PHYS_SDRAM                      0x40000000
#define PHYS_SDRAM_SIZE                 SZ_2G
#define PHYS_SDRAM_2                    0x100000000
#define PHYS_SDRAM_2_SIZE               SZ_2G

#define CFG_MXC_UART_BASE               UART2_BASE_ADDR

#ifdef CONFIG_ANDROID_SUPPORT
#include "imx8mp_summitsom_android.h"
#endif

#endif
