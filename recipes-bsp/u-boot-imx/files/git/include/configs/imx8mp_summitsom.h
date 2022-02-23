/* SPDX-License-Identifier: GPL-2.0+ */
/*
 * Copyright 2019 NXP
 */

#ifndef __IMX8MP_SUMMITSOM_H
#define __IMX8MP_SUMMITSOM_H

#include <linux/sizes.h>
#include <linux/stringify.h>
#include <asm/arch/imx-regs.h>
#include "imx_env.h"

#define CONFIG_SYS_BOOTM_LEN		(32 * SZ_1M)

#define CONFIG_SPL_MAX_SIZE		(152 * 1024)
#define CONFIG_SYS_MONITOR_LEN		(512 * 1024)
#define CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_USE_SECTOR
#define CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_SECTOR	0x300
#define CONFIG_SYS_UBOOT_BASE	(QSPI0_AMBA_BASE + CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_SECTOR * 512)

#ifdef CONFIG_SPL_BUILD
#define CONFIG_SPL_STACK		0x96dff0
#define CONFIG_SPL_BSS_START_ADDR      0x96e000
#define CONFIG_SPL_BSS_MAX_SIZE		SZ_8K	/* 8 KB */
#define CONFIG_SYS_SPL_MALLOC_START	0x42200000
#define CONFIG_SYS_SPL_MALLOC_SIZE	SZ_512K	/* 512 KB */

/* For RAW image gives a error info not panic */
#define CONFIG_SPL_ABORT_ON_RAW_IMAGE

#endif

#define CONFIG_CMD_READ
#define CONFIG_SERIAL_TAG
#define CONFIG_FASTBOOT_USB_DEV 0

#define CONFIG_REMAKE_ELF
/* ENET Config */
/* ENET1 */
#if defined(CONFIG_CMD_NET)
//#define CONFIG_ETHPRIME                 "eth1" /* Set eqos to primary since we use its MDIO */

#define CONFIG_FEC_XCV_TYPE             RGMII

#define PHY_ANEG_TIMEOUT 20000

#endif

#ifdef CONFIG_DISTRO_DEFAULTS
#ifdef CONFIG_CMD_USB
#define BOOT_TARGET_DEVICES(func) \
	func(USB, usb, 0) \
	func(MMC, mmc, 1) \
	func(MMC, mmc, 2)
#else
#define BOOT_TARGET_DEVICES(func) \
	func(MMC, mmc, 1) \
	func(MMC, mmc, 2)
#endif
#include <config_distro_bootcmd.h>
#else
#define BOOTENV
#endif


#define JAILHOUSE_ENV \
	"jh_clk= \0 " \
	"jh_mmcboot=setenv fdtfile imx8mp-summitsom-root.dtb;" \
		"setenv jh_clk clk_ignore_unused mem=2048MB; " \
			   "if run loadimage; then " \
				   "run mmcboot; " \
			   "else run jh_netboot; fi; \0" \
	"jh_netboot=setenv fdtfile imx8mp-summitsom-root.dtb; setenv jh_clk clk_ignore_unused mem=2048MB; run netboot; \0 "

#define CONFIG_MFG_ENV_SETTINGS \
	CONFIG_MFG_ENV_SETTINGS_DEFAULT \
	"initrd_addr=0x43800000\0" \
	"initrd_high=0xffffffffffffffff\0" \
	"emmc_dev=2\0"\
	"sd_dev=1\0" \


#define CONFIG_EXTRA_ENV_SETTINGS			\
	CONFIG_MFG_ENV_SETTINGS				\
	"autoload=no\0"					\
	"autostart=no\0" 				\
	"console=ttymxc1,115200\0"			\
	"scriptaddr=0x43500000\0"			\
	"fdt_addr=0x43000000\0"				\
	"splashimage=0x50000000\0"			\
	"conf=conf-freescale_imx8mp-summitsom-dvk-pcie-uart.dtb\0" \
	"loadimage=load mmc ${mmcdev}:${bootvol} ${loadaddr} fitImage\0" \
	"mmcargs="					\
		"setenv bootargs console=${console} "	\
		"root=/dev/mmcblk${mmcdev}p${rootvol} " \
		"rootfstype=squashfs rootwait ro quiet "\
		"bootside=${bootside} "			\
		"init=/usr/sbin/overlayRoot.sh\0"	\
	"mmcside="					\
		"if test ${bootside} = a; then "	\
			"setenv bootvol 1; "		\
		"else "					\
			"setenv bootvol 4; "		\
		"fi; "					\
		"setexpr rootvol ${bootvol} + 1;\0"	\
	"bootcmd="					\
		"run mmcside; "				\
		"if run loadimage; then " 		\
			"run mmcargs; "			\
			"bootm ${loadaddr}#${conf}; "	\
	   	"fi; "

/* Link Definitions */
#define CONFIG_LOADADDR			0x44000000

#define CONFIG_SYS_LOAD_ADDR		CONFIG_LOADADDR

#define CONFIG_SYS_INIT_RAM_ADDR	0x40000000
#define CONFIG_SYS_INIT_RAM_SIZE	0x80000
#define CONFIG_SYS_INIT_SP_OFFSET \
	(CONFIG_SYS_INIT_RAM_SIZE - GENERATED_GBL_DATA_SIZE)
#define CONFIG_SYS_INIT_SP_ADDR \
	(CONFIG_SYS_INIT_RAM_ADDR + CONFIG_SYS_INIT_SP_OFFSET)

#define CONFIG_ENV_SPI_BUS		CONFIG_SF_DEFAULT_BUS
#define CONFIG_ENV_SPI_CS		CONFIG_SF_DEFAULT_CS
#define CONFIG_ENV_SPI_MODE		CONFIG_SF_DEFAULT_MODE
#define CONFIG_ENV_SPI_MAX_HZ		CONFIG_SF_DEFAULT_SPEED

#define CONFIG_SYS_MMC_ENV_PART		0

/* Size of malloc() pool */
#define CONFIG_SYS_MALLOC_LEN		SZ_32M

/* Totally 2GB DDR */
#define CONFIG_SYS_SDRAM_BASE		0x40000000

#define PHYS_SDRAM			0x40000000

#if defined(CONFIG_SYS_SUMMITSOM_2G)
#define PHYS_SDRAM_SIZE			0x80000000	/* 2 GB */
#elif defined(CONFIG_SYS_SUMMITSOM_1G)
#define PHYS_SDRAM_SIZE			0x40000000	/* 1 GB */
#elif defined(CONFIG_SYS_SUMMITSOM_512M)
#define PHYS_SDRAM_SIZE			0x20000000	/* 512 MB */
#endif

#define CONFIG_MXC_UART_BASE		UART2_BASE_ADDR

/* Monitor Command Prompt */
#define CONFIG_SYS_CBSIZE		2048
#define CONFIG_SYS_MAXARGS		64
#define CONFIG_SYS_BARGSIZE		CONFIG_SYS_CBSIZE
#define CONFIG_SYS_PBSIZE		(CONFIG_SYS_CBSIZE + \
					sizeof(CONFIG_SYS_PROMPT) + 16)

#define CONFIG_IMX_BOOTAUX

#define CONFIG_SYS_FSL_USDHC_NUM	2
#define CONFIG_SYS_FSL_ESDHC_ADDR	0

#define CONFIG_SYS_MMC_IMG_LOAD_PART	1

#define CONFIG_SYS_I2C_SPEED		100000

#define CONFIG_USB_MAX_CONTROLLER_COUNT         2
#define CONFIG_USBD_HS
#define CONFIG_USB_GADGET_VBUS_DRAW 2

#ifdef CONFIG_DM_VIDEO
#define CONFIG_VIDEO_LOGO
#define CONFIG_BMP_16BPP
#define CONFIG_BMP_24BPP
#define CONFIG_BMP_32BPP
#define CONFIG_VIDEO_BMP_RLE8
#define CONFIG_VIDEO_BMP_LOGO
#endif

#ifdef CONFIG_ANDROID_SUPPORT
#include "imx8mp_summitsom_android.h"
#endif

#endif
