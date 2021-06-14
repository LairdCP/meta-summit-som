#!/bin/sh

echo "Loading Drivers"

dtb=$(fw_printenv -n fdt_file)

modprobe -a cfg80211 hci_uart

case "${dtb}" in
*sdio*)
   insmod /lib/modules/nxp/sdio/mlan.ko
   insmod /lib/modules/nxp/sdio/sd8xxx cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=nxp/sdio8997_uart_combo.bin
   ;;

*pcie*) 
   insmod /lib/modules/nxp/pcie/mlan.ko
   insmod /lib/modules/nxp/pcie/pcie8xxx.ko cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=nxp/pcie8997_uart_combo.bin
   ;;
esac

btattach -B /dev/ttymxc2 -P h4 -S 3000000 &
