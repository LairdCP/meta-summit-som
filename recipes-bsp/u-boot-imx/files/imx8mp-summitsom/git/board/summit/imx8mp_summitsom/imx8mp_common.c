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

static int get_boot_side(int dev)
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

static void set_bootside(void)
{
	const char *side;
	enum boot_device bdev = get_boot_device();
	int devno;

	switch (bdev) {
	case SD1_BOOT:
	case SD2_BOOT:
	case SD3_BOOT:
		devno = bdev - SD1_BOOT;
		env_set_ulong("mmcdev", devno);
		env_set("bootside", "a");
		printf("Booting from SD, side a\n");
		break;

	case MMC1_BOOT:
	case MMC2_BOOT:
	case MMC3_BOOT:
		devno = bdev - MMC1_BOOT;
		env_set_ulong("mmcdev", devno);
		side = get_boot_side(devno) == 2 ? "b" : "a";
		env_set("bootside", side);
		printf("Booting from eMMC, side %s\n", side);
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
