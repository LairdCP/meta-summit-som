#define UART_PAD_CTRL   (PAD_CTL_DSE6 | PAD_CTL_FSEL1)

static iomux_v3_cfg_t const uart_pads[] = {
	MX8MP_PAD_UART2_RXD__UART2_DCE_RX | MUX_PAD_CTRL(UART_PAD_CTRL),
	MX8MP_PAD_UART2_TXD__UART2_DCE_TX | MUX_PAD_CTRL(UART_PAD_CTRL),
};

static void early_uart_init(void)
{
	imx_iomux_v3_setup_multiple_pads(uart_pads, ARRAY_SIZE(uart_pads));

	init_uart_clk(1);
}

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
