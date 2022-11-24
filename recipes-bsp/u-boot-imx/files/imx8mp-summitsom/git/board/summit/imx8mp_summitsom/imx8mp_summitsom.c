// SPDX-License-Identifier: GPL-2.0+
/*
 * Copyright 2019 NXP
 */

#include <common.h>
#include <errno.h>
#include <miiphy.h>
#include <netdev.h>
#include <init.h>
#include <asm/io.h>
#include <asm/mach-imx/iomux-v3.h>
#include <asm-generic/gpio.h>
#include <asm/arch/imx8mp_pins.h>
#include <asm/arch/clock.h>
#include <asm/mach-imx/boot_mode.h>
#include <asm/arch/sys_proto.h>
#include <asm/mach-imx/gpio.h>
#include <asm/mach-imx/mxc_i2c.h>
#include <spl.h>
#include <asm/mach-imx/dma.h>
#include <power/pmic.h>
#include "../../freescale/common/tcpc.h"
#include <usb.h>
#include <dwc3-uboot.h>
#include <imx_sip.h>
#include <linux/arm-smccc.h>
#include <mmc.h>
#include <fuse.h>

DECLARE_GLOBAL_DATA_PTR;

#define UART_PAD_CTRL   (PAD_CTL_DSE6 | PAD_CTL_FSEL1)
#define WDOG_PAD_CTRL   (PAD_CTL_DSE6 | PAD_CTL_ODE | PAD_CTL_PUE | PAD_CTL_PE)

static iomux_v3_cfg_t const uart_pads[] = {
	MX8MP_PAD_UART2_RXD__UART2_DCE_RX | MUX_PAD_CTRL(UART_PAD_CTRL),
	MX8MP_PAD_UART2_TXD__UART2_DCE_TX | MUX_PAD_CTRL(UART_PAD_CTRL),
};

static iomux_v3_cfg_t const wdog_pads[] = {
	MX8MP_PAD_GPIO1_IO02__WDOG1_WDOG_B | MUX_PAD_CTRL(WDOG_PAD_CTRL),
};

int board_early_init_f(void)
{
	struct wdog_regs *wdog = (struct wdog_regs *)WDOG1_BASE_ADDR;

	imx_iomux_v3_setup_multiple_pads(wdog_pads, ARRAY_SIZE(wdog_pads));

	set_wdog_reset(wdog);

	imx_iomux_v3_setup_multiple_pads(uart_pads, ARRAY_SIZE(uart_pads));

	init_uart_clk(1);

	return 0;
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

#ifdef CONFIG_OF_BOARD_SETUP
int ft_board_setup(void *blob, struct bd_info *bd)
{
#ifdef CONFIG_IMX8M_DRAM_INLINE_ECC
	int rc;
	phys_addr_t ecc0_start = 0xb0000000;
	phys_addr_t ecc1_start = 0x130000000;
	phys_addr_t ecc2_start = 0x1b0000000;
	size_t ecc_size = 0x10000000;

	rc = add_res_mem_dt_node(blob, "ecc", ecc0_start, ecc_size);
	if (rc < 0) {
		printf("Could not create ecc0 reserved-memory node.\n");
		return rc;
	}

	rc = add_res_mem_dt_node(blob, "ecc", ecc1_start, ecc_size);
	if (rc < 0) {
		printf("Could not create ecc1 reserved-memory node.\n");
		return rc;
	}

	rc = add_res_mem_dt_node(blob, "ecc", ecc2_start, ecc_size);
	if (rc < 0) {
		printf("Could not create ecc2 reserved-memory node.\n");
		return rc;
	}
#endif

	return 0;
}
#endif

static int setup_charger(uint8_t i2c_bus, uint8_t addr)
{
	struct udevice *bus;
	struct udevice *i2c_dev = NULL;
	int ret;
	uint8_t valb;

	ret = uclass_get_device_by_seq(UCLASS_I2C, i2c_bus, &bus);
	if (ret) {
		printf("%s: Can't find bus\n", __func__);
		return -EINVAL;
	}

	ret = dm_i2c_probe(bus, addr, 0, &i2c_dev);
	if (ret) {
		printf("%s: Can't find device id=0x%x\n",
		       __func__, addr);
		return -ENODEV;
	}

	ret = dm_i2c_read(i2c_dev, 0x48, &valb, 1);
	if (ret) {
		printf("%s dm_i2c_read failed, err %d\n", __func__, ret);
		return -EIO;
	}

	if (valb != 0)
		return 0;

	valb = 5;
	ret = dm_i2c_write(i2c_dev, 0x10, &valb, 1);
	if (ret) {
		printf("%s dm_i2c_write failed, err %d\n", __func__, ret);
		return -EIO;
	}

	valb = 0;
	ret = dm_i2c_write(i2c_dev, 0x11, &valb, 1);
	if (ret) {
		printf("%s dm_i2c_write failed, err %d\n", __func__, ret);
		return -EIO;
	}

	return 0;
}


#ifdef CONFIG_USB_TCPC
struct tcpc_port port1;

static int setup_pd_switch(uint8_t i2c_bus, uint8_t addr)
{
	struct udevice *bus;
	struct udevice *i2c_dev = NULL;
	int ret;
	uint8_t valb;

	ret = uclass_get_device_by_seq(UCLASS_I2C, i2c_bus, &bus);
	if (ret) {
		printf("%s: Can't find bus\n", __func__);
		return -EINVAL;
	}

	ret = dm_i2c_probe(bus, addr, 0, &i2c_dev);
	if (ret) {
		printf("%s: Can't find device id=0x%x\n", __func__, addr);
		return -ENODEV;
	}

	ret = dm_i2c_read(i2c_dev, 0xB, &valb, 1);
	if (ret) {
		printf("%s dm_i2c_read failed, err %d\n", __func__, ret);
		return -EIO;
	}
	valb |= 0x4; /* Set DB_EXIT to exit dead battery mode */
	ret = dm_i2c_write(i2c_dev, 0xB, (const uint8_t *)&valb, 1);
	if (ret) {
		printf("%s dm_i2c_write failed, err %d\n", __func__, ret);
		return -EIO;
	}

	/* Set OVP threshold to 23V */
	valb = 0x6;
	ret = dm_i2c_write(i2c_dev, 0x8, (const uint8_t *)&valb, 1);
	if (ret) {
		printf("%s dm_i2c_write failed, err %d\n", __func__, ret);
		return -EIO;
	}

	return 0;
}

int pd_switch_snk_enable(struct tcpc_port *port)
{
	return setup_pd_switch(1, 0x72);
}

struct tcpc_port_config port1_config = {
	.i2c_bus		= 1, /*i2c2*/
	.addr			= 0x50,
	.port_type		= TYPEC_PORT_UFP,
	.max_snk_mv		= 20000,
	.max_snk_ma		= 3000,
	.max_snk_mw		= 45000,
	.op_snk_mv		= 15000,
	.switch_setup_func	= &pd_switch_snk_enable,
	.disable_pd		= true,
};

#define USB_TYPEC_SEL IMX_GPIO_NR(4, 20)
#define USB_TYPEC_EN IMX_GPIO_NR(2, 20)

static iomux_v3_cfg_t ss_mux_gpio[] = {
	MX8MP_PAD_SAI1_MCLK__GPIO4_IO20 | MUX_PAD_CTRL(NO_PAD_CTRL),
	MX8MP_PAD_SD2_WP__GPIO2_IO20 | MUX_PAD_CTRL(NO_PAD_CTRL),
};

void ss_mux_select(enum typec_cc_polarity pol)
{
	if (pol == TYPEC_POLARITY_CC1)
		gpio_direction_output(USB_TYPEC_SEL, 0);
	else
		gpio_direction_output(USB_TYPEC_SEL, 1);
}

static int setup_typec(void)
{
	int ret;

	debug("tcpc_init port 1\n");
	imx_iomux_v3_setup_multiple_pads(ss_mux_gpio, ARRAY_SIZE(ss_mux_gpio));
	gpio_request(USB_TYPEC_SEL, "typec_sel");
	gpio_request(USB_TYPEC_EN, "typec_en");
	gpio_direction_output(USB_TYPEC_EN, 0);

	ret = tcpc_init(&port1, port1_config, &ss_mux_select);
	if (ret) {
		printf("%s: tcpc port1 init failed, err=%d\n",
		       __func__, ret);
	} else {
		return ret;
	}

	return ret;
}
#endif

#ifdef CONFIG_USB_DWC3

#define USB_PHY_CTRL0                   0xF0040
#define USB_PHY_CTRL0_REF_SSP_EN        BIT(2)

#define USB_PHY_CTRL1                   0xF0044
#define USB_PHY_CTRL1_RESET             BIT(0)
#define USB_PHY_CTRL1_COMMONONN         BIT(1)
#define USB_PHY_CTRL1_ATERESET          BIT(3)
#define USB_PHY_CTRL1_VDATSRCENB0       BIT(19)
#define USB_PHY_CTRL1_VDATDETENB0       BIT(20)

#define USB_PHY_CTRL2                   0xF0048
#define USB_PHY_CTRL2_TXENABLEN0        BIT(8)

#define USB_PHY_CTRL6                   0xF0058

#define HSIO_GPR_BASE                               (0x32F10000U)
#define HSIO_GPR_REG_0                              (HSIO_GPR_BASE)
#define HSIO_GPR_REG_0_USB_CLOCK_MODULE_EN_SHIFT    (1)
#define HSIO_GPR_REG_0_USB_CLOCK_MODULE_EN          (0x1U << HSIO_GPR_REG_0_USB_CLOCK_MODULE_EN_SHIFT)


static struct dwc3_device dwc3_device_data = {
#ifdef CONFIG_SPL_BUILD
	.maximum_speed		= USB_SPEED_HIGH,
#else
	.maximum_speed		= USB_SPEED_SUPER,
#endif
	.base			= USB1_BASE_ADDR,
	.dr_mode		= USB_DR_MODE_PERIPHERAL,
	.index			= 0,
	.power_down_scale	= 2,
};

int usb_gadget_handle_interrupts(int index)
{
	dwc3_uboot_handle_interrupt(index);
	return 0;
}

static void dwc3_nxp_usb_phy_init(struct dwc3_device *dwc3)
{
	u32 RegData;

	/* enable usb clock via hsio gpr */
	RegData = readl(HSIO_GPR_REG_0);
	RegData |= HSIO_GPR_REG_0_USB_CLOCK_MODULE_EN;
	writel(RegData, HSIO_GPR_REG_0);

	/* USB3.0 PHY signal fsel for 100M ref */
	RegData = readl(dwc3->base + USB_PHY_CTRL0);
	RegData = (RegData & 0xfffff81f) | (0x2a << 5);
	writel(RegData, dwc3->base + USB_PHY_CTRL0);

	RegData = readl(dwc3->base + USB_PHY_CTRL6);
	RegData &= ~0x1;
	writel(RegData, dwc3->base + USB_PHY_CTRL6);

	RegData = readl(dwc3->base + USB_PHY_CTRL1);
	RegData &= ~(USB_PHY_CTRL1_VDATSRCENB0 | USB_PHY_CTRL1_VDATDETENB0 |
		     USB_PHY_CTRL1_COMMONONN);
	RegData |= USB_PHY_CTRL1_RESET | USB_PHY_CTRL1_ATERESET;
	writel(RegData, dwc3->base + USB_PHY_CTRL1);

	RegData = readl(dwc3->base + USB_PHY_CTRL0);
	RegData |= USB_PHY_CTRL0_REF_SSP_EN;
	writel(RegData, dwc3->base + USB_PHY_CTRL0);

	RegData = readl(dwc3->base + USB_PHY_CTRL2);
	RegData |= USB_PHY_CTRL2_TXENABLEN0;
	writel(RegData, dwc3->base + USB_PHY_CTRL2);

	RegData = readl(dwc3->base + USB_PHY_CTRL1);
	RegData &= ~(USB_PHY_CTRL1_RESET | USB_PHY_CTRL1_ATERESET);
	writel(RegData, dwc3->base + USB_PHY_CTRL1);
}
#endif

#if defined(CONFIG_USB_DWC3) || defined(CONFIG_USB_XHCI_IMX8M)
int board_usb_init(int index, enum usb_init_type init)
{
	int ret = 0;

	imx8m_usb_power(index, true);

	if (index == 0 && init == USB_INIT_DEVICE) {
#ifdef CONFIG_USB_TCPC
		ret = tcpc_setup_ufp_mode(&port1);
		if (ret)
			return ret;
#endif
		dwc3_nxp_usb_phy_init(&dwc3_device_data);
		return dwc3_uboot_init(&dwc3_device_data);
	} else if (index == 0 && init == USB_INIT_HOST) {
#ifdef CONFIG_USB_TCPC
		ret = tcpc_setup_dfp_mode(&port1);
#endif
		return ret;
	}

	return 0;
}

int board_usb_cleanup(int index, enum usb_init_type init)
{
	int ret = 0;

	if (index == 0 && init == USB_INIT_DEVICE) {
		dwc3_uboot_exit(index);
	} else if (index == 0 && init == USB_INIT_HOST) {
#ifdef CONFIG_USB_TCPC
		ret = tcpc_disable_src_vbus(&port1);
#endif
	}

	imx8m_usb_power(index, false);

	return ret;
}

#ifdef CONFIG_USB_TCPC
/* Not used so far */
int board_typec_get_mode(int index)
{
	int ret = 0;
	enum typec_cc_polarity pol;
	enum typec_cc_state state;

	if (index == 0) {
		tcpc_setup_ufp_mode(&port1);

		ret = tcpc_get_cc_status(&port1, &pol, &state);
		if (!ret) {
			if (state == TYPEC_STATE_SRC_RD_RA || state == TYPEC_STATE_SRC_RD)
				return USB_INIT_HOST;
		}

		return USB_INIT_DEVICE;
	} else {
		return USB_INIT_HOST;
	}
}
#endif
#endif

#if CONFIG_IS_ENABLED(FEC_MXC)
static void setup_fec(void)
{
	struct iomuxc_gpr_base_regs *gpr =
		(struct iomuxc_gpr_base_regs *)IOMUXC_GPR_BASE_ADDR;

	/* Enable RGMII TX clk output */
	setbits_le32(&gpr->gpr[1], BIT(22));
}
#endif

#if CONFIG_IS_ENABLED(DWC_ETH_QOS)
static int setup_eqos(void)
{
	struct iomuxc_gpr_base_regs *gpr =
		(struct iomuxc_gpr_base_regs *)IOMUXC_GPR_BASE_ADDR;

	/* set INTF as RGMII, enable RGMII TXC clock */
	clrsetbits_le32(&gpr->gpr[1],
			IOMUXC_GPR_GPR1_GPR_ENET_QOS_INTF_SEL_MASK, BIT(16));
	setbits_le32(&gpr->gpr[1], BIT(19) | BIT(21));

	return set_clk_eqos(ENET_125MHZ);
}
#endif

#if CONFIG_IS_ENABLED(NET)
int board_phy_config(struct phy_device *phydev)
{
	if (phydev->drv->config)
		phydev->drv->config(phydev);
	return 0;
}
#endif

#define DISPMIX                         13
#define MIPI                            15

static bool wbx3;

int board_init(void)
{
	struct arm_smccc_res res;

	wbx3 = setup_charger(1, 0x6b) < 0;

#ifdef CONFIG_USB_TCPC
	if (!wbx3)
		setup_typec();
#endif

#if CONFIG_IS_ENABLED(FEC_MXC)
	setup_fec();
#endif

#if CONFIG_IS_ENABLED(DWC_ETH_QOS)
	if (!wbx3)
		setup_eqos();
#endif

#if defined(CONFIG_USB_DWC3) || defined(CONFIG_USB_XHCI_IMX8M)
	if (!wbx3)
		init_usb_clk();
#endif

	/* enable the dispmix & mipi phy power domain */
	arm_smccc_smc(IMX_SIP_GPC, IMX_SIP_GPC_PM_DOMAIN,
		      DISPMIX, true, 0, 0, 0, 0, &res);
	arm_smccc_smc(IMX_SIP_GPC, IMX_SIP_GPC_PM_DOMAIN,
		      MIPI, true, 0, 0, 0, 0, &res);

	return 0;
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

#ifdef CONFIG_BLOCK_CACHE
	struct blk_desc *bd = mmc_get_blk_desc(mmc);
	blkcache_invalidate(bd->if_type, bd->devnum);
#endif

	if (IS_SD(mmc))
		return 0;

	return EXT_CSD_EXTRACT_BOOT_PART(mmc->part_config);
}

uint mmc_get_env_part(struct mmc *mmc)
{
	switch (get_boot_device()) {
	case MMC3_BOOT:
		return get_boot_side(2);
	default:
		return CONFIG_SYS_MMC_ENV_PART;
	}
}

int board_late_init(void)
{
	const char *side;

	switch (get_boot_device()) {
	case SD2_BOOT:
		env_set_ulong("mmcdev", 1);
		env_set("bootside", "a");
		printf("Booting from SD, side a\n");
		break;
	case MMC3_BOOT:
		env_set_ulong("mmcdev", 2);
		side = get_boot_side(2) == 2 ? "b" : "a";
		env_set("bootside", side);
		printf("Booting from eMMC, side %s\n", side);
		break;
	default:
		break;
	}

#ifdef CONFIG_ENV_VARS_UBOOT_RUNTIME_CONFIG
	env_set("board_name", "Summit SOM");
	env_set("board_rev", "iMX8MP");
#endif

	if (wbx3)
		update_dts("dvk", "wbx");
	else
		update_dts("wbx", "dvk");

	if (gd->flags & GD_FLG_ENV_DEFAULT) {
		puts("Saving default environment...\n");
		env_save();
	}

	return 0;
}

#ifdef CONFIG_ANDROID_SUPPORT
bool is_power_key_pressed(void)
{
	return (bool)(!!(readl(SNVS_HPSR) & (0x1 << 6)));
}
#endif

#ifdef CONFIG_SPL_MMC_SUPPORT
#define UBOOT_RAW_SECTOR_OFFSET 0x40
unsigned long spl_mmc_get_uboot_raw_sector(struct mmc *mmc)
{
	u32 boot_dev = spl_boot_device();

	switch (boot_dev) {
	case BOOT_DEVICE_MMC2:
		return CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_SECTOR - UBOOT_RAW_SECTOR_OFFSET;
	default:
		return CONFIG_SYS_MMCSD_RAW_MODE_U_BOOT_SECTOR;
	}
}
#endif

#ifdef CONFIG_FSL_FASTBOOT
#ifdef CONFIG_ANDROID_RECOVERY
int is_recovery_key_pressing(void)
{
	return 0; /* TODO */
}
#endif /* CONFIG_ANDROID_RECOVERY */
#endif /* CONFIG_FSL_FASTBOOT */
