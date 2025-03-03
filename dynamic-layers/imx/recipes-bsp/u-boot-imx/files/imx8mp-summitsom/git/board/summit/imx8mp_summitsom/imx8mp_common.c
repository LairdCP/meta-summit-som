// SPDX-License-Identifier: GPL-2.0+
/*
 * Board specific initialization for Carbon AM62 OSM module
 *
 * Copyright (C) 2023 Ezurio
 *
 */

#ifndef CONFIG_TARGET_IMX8MP_EVK_RDVK
#include <fuse.h>

int board_phys_sdram_size(phys_size_t *memsize)
{
	u32 gp1 = 0;

	fuse_read(14, 0, &gp1);

	switch (gp1 & 0xff) {
	case 1:
		*memsize = SZ_1G;
		break;
	case 2:
	case 5:
		*memsize = SZ_2G;
		break;
	case 3:
		*memsize = SZ_4G;
		break;
	case 4:
		*memsize = SZ_512M;	
		break;
	default:
		if ((readl(0x3d400000) & 0xf000000) == 0x3000000)
			*memsize = SZ_4G;
		else
			*memsize = get_ram_size((void *)PHYS_SDRAM, SZ_2G);
		break;
	}

	return 0;
}
#endif

static int __maybe_unused get_boot_side(int dev)
{
	struct mmc *mmc;

	mmc = find_mmc_device(dev);
	if (!mmc)
		return 0;

	if (!mmc_getcd(mmc))
		mmc->has_init = 0;

	if (mmc_init(mmc))
		return 0;

	if (IS_SD(mmc))
		return 0;

	return EXT_CSD_EXTRACT_BOOT_PART(mmc->part_config);
}

#ifdef CONFIG_SYS_MMC_ENV_PART
uint mmc_get_env_part(struct mmc *mmc)
{
	enum boot_device bdev = get_boot_device();
	int devno;

	switch (bdev) {
	case SD1_BOOT:
	case SD2_BOOT:
	case SD3_BOOT:
		return CONFIG_SYS_MMC_ENV_PART;

	case MMC1_BOOT:
	case MMC2_BOOT:
	case MMC3_BOOT:
		devno = bdev - MMC1_BOOT;
		return get_boot_side(devno);

	default:
		return 0;
	}
}
#endif

enum env_location env_get_location(enum env_operation op, int prio)
{
	u32 bdev = get_boot_device();

	if (prio)
		return ENVL_UNKNOWN;

	switch (bdev) {
	case SD1_BOOT:
	case SD2_BOOT:
	case SD3_BOOT:
		if (CONFIG_IS_ENABLED(ENV_IS_IN_FAT))
			return ENVL_FAT;
		else
			return ENVL_NOWHERE;

	case MMC1_BOOT:
	case MMC2_BOOT:
	case MMC3_BOOT:
		if (CONFIG_IS_ENABLED(ENV_IS_IN_MMC))
			return ENVL_MMC;

	default:
		return ENVL_NOWHERE;
	};
}

static void set_bootside(void)
{
	enum boot_device bdev = get_boot_device();
	int devno, side;

	switch (bdev) {
	case SD1_BOOT:
	case SD2_BOOT:
	case SD3_BOOT:
		devno = bdev - SD1_BOOT;
		env_set_ulong("mmcdev", devno);
		env_set("boot_src", "sd");
		env_set("bootside", "a");
		printf("Booting from SD, side a\n");
		break;

	case MMC1_BOOT:
	case MMC2_BOOT:
	case MMC3_BOOT:
		devno = bdev - MMC1_BOOT;
		env_set_ulong("mmcdev", devno);
		env_set("boot_src", "emmc");
		side = get_boot_side(devno);
		env_set("bootside", side == 2 ? "b" : "a");
		printf("Booting from eMMC, side %s\n", side == 2 ? "b" : "a");
		break;

	default:
		break;
	}
}

static void update_dts(const char *oldn, const char *newn)
{
	char buf[64], *dvk;

	char *dtb = env_get("conf");

	if (!strstr(dtb, oldn))
		return;

	strncpy(buf, dtb, sizeof(buf));
	buf[sizeof(buf) - 1] = 0;
	dvk = strstr(buf, oldn);
	if (!dvk)
		return;

	memcpy(dvk, newn, 3);

	env_set("conf", buf);
	env_save();
}
