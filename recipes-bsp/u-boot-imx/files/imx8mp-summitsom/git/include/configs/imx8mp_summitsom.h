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

#define CFG_SYS_UBOOT_BASE   (QSPI0_AMBA_BASE + CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_SECTOR * 512)

/* ENET Config */
/* ENET1 */
#if defined(CONFIG_CMD_NET)
//#define CFG_ETHPRIME                 "eth1" /* Set eqos to primary since we use its MDIO */

//#define CFG_FEC_XCV_TYPE             RGMII

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

#define JH_ROOT_DTB    "imx8mp-summitsom-root.dtb"

#define JAILHOUSE_ENV \
	"jh_clk= \0 " \
	"jh_root_dtb=" JH_ROOT_DTB "\0" \
	"jh_mmcboot=setenv fdtfile ${jh_root_dtb};" \
		"setenv jh_clk clk_ignore_unused mem=1920MB; " \
			   "if run loadimage; then " \
				   "run mmcboot; " \
			   "else run jh_netboot; fi; \0" \
	"jh_netboot=setenv fdtfile ${jh_root_dtb}; setenv jh_clk clk_ignore_unused mem=1920MB; run netboot; \0 "

#define CFG_MFG_ENV_SETTINGS \
	CFG_MFG_ENV_SETTINGS_DEFAULT \
	"initrd_addr=0x43800000\0" \
	"initrd_high=0xffffffffffffffff\0" \
	"emmc_dev=2\0" \
	"sd_dev=1\0"

#define EXTRA_ENV_SETTINGS_DEFAULT                  \
	"autoload=no\0"                                 \
	"autostart=no\0"                                \
	"console=ttymxc1,115200\0"                      \
	"scriptaddr=0x43500000\0"                       \
	"fdt_addr=0x43000000\0"                         \
	"fdt_high=0xffffffffffffffff\0"                 \
	"bootm_size=0x10000000\0"                       \
	"m7_bootaddr_itcm=0x7e0000\0"                   \
	"m7_bootaddr_ddr=0x80000000\0"                  \
	"m7_bootaddr_qspi=0x08000000\0"                 \
	"m7_ddr_temp=0x48000000\0"                      \
	"bootm7_itcm=imxtract ${loadaddr} firmware-1 ${m7_ddr_temp}; cp.b ${m7_ddr_temp} ${m7_bootaddr_itcm} 20000; bootaux ${m7_bootaddr_itcm}\0" \
	"bootm7_ddr=imxtract ${loadaddr} firmware-1 ${m7_bootaddr_ddr}; dcache flush; bootaux ${m7_bootaddr_ddr}\0" \
	"bootm7_qspi=sf probe; sf read ${m7_ddr_temp} 0 0x100000; bootaux ${m7_bootaddr_qspi}\0" \
	"splashimage=0x50000000\0"                      \
	"conf=conf-freescale_imx8mp-summitsom-dvk-pcie-uart.dtb\0" \
	"loadimage=load mmc ${mmcdev}:${bootvol} ${loadaddr} fitImage\0" \
	"loadverity=load mmc ${mmcdev}:${bootvol} ${loadaddr} fitImageVerity.bin\0" \
	"loadm7=load mmc ${mmcdev}:${bootvol} ${loadaddr} fitImageMcu.bin\0" \
	"mmcside="                                      \
	"if test ${bootside} = a; then "                \
	"setenv bootvol 1; "                            \
	"else "                                         \
	"setenv bootvol 4; "                            \
	"fi; "                                          \
	"setexpr rootvol ${bootvol} + 1\0"              \
	"runm7="                                        \
	"setexpr bootm7 sub m7-rpmsg '' $conf; "        \
	"if test -n \"${bootm7}\"; then "               \
	"run loadm7 && run bootm7_itcm; "               \
	"fi\0"

#ifdef CONFIG_SUMMIT_SECURE
#define CFG_EXTRA_ENV_SETTINGS                      \
	EXTRA_ENV_SETTINGS_DEFAULT                      \
	"mmcargs="                                      \
	"setenv bootargs console=${console} quiet "     \
	"bootside=${bootside} "                         \
	"init=/usr/sbin/pre-systemd-init.sh\0"          \
	"bootcmd="                                      \
	"run mmcside; "                                 \
	"run runm7; "                                   \
	"if test -e mmc ${mmcdev}:${bootvol} fitImageVerity.bin; then " \
	"run mmcargs; "                                 \
	"run loadverity && source ${loadaddr}:script-1; " \
	"run loadimage && bootm ${loadaddr}#${conf}; "  \
	"fi"
#else
#define CFG_EXTRA_ENV_SETTINGS                      \
	EXTRA_ENV_SETTINGS_DEFAULT                      \
	"mmcargs="                                      \
	"setenv bootargs console=${console} quiet "     \
	"bootside=${bootside} "                         \
	"init=/usr/sbin/overlayRoot.sh\0"               \
	"mmcargs_trad="                                 \
	"setenv bootargs console=${console} quiet "     \
	"bootside=${bootside} "                         \
	"root=/dev/mmcblk${mmcdev}p${rootvol} "         \
	"rootwait rootfstype=ext4 rw\0"                 \
	"bootcmd="                                      \
	"run mmcside; "                                 \
	"run runm7; "                                   \
	"if test -e mmc ${mmcdev}:${bootvol} fitImageVerity.bin; then " \
	"run mmcargs; "                                 \
	"run loadverity && source ${loadaddr}:script-1; " \
	"run loadimage && bootm ${loadaddr}#${conf}; "  \
	"elif test ${mmcdev} = 1; then "                \
	"run mmcargs_trad; "                            \
	"run loadimage && bootm ${loadaddr}#${conf}; "  \
	"fi"
#endif

/* Link Definitions */

#define CFG_SYS_INIT_RAM_ADDR        0x40000000
#define CFG_SYS_INIT_RAM_SIZE        0x80000

/* Totally 2GB DDR */
#define CFG_SYS_SDRAM_BASE              0x40000000

#define PHYS_SDRAM                      0x40000000
#define PHYS_SDRAM_SIZE                 SZ_2G
#define PHYS_SDRAM_2                    0x100000000
#define PHYS_SDRAM_2_SIZE               SZ_2G

#define CFG_MXC_UART_BASE               UART2_BASE_ADDR


#define CFG_SYS_FSL_USDHC_NUM           2


#ifdef CONFIG_ANDROID_SUPPORT
#include "imx8mp_summitsom_android.h"
#endif

#endif
