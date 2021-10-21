#!/bin/sh

fwName() {
   f=$(ls /lib/firmware/lrdmwl/88W8997_mfg_${1}_*)
   fw="lrdmwl/${f##*/}"
}

echo "Loading Mfg Bridge Drivers"

dtb=$(fw_printenv -n fdtfile)

modprobe -a cfg80211 bluetooth

case "${dtb}" in
*sdio-uart*)
   [ "${1}" == lrd ] && fwName sdio_uart || fw=nxp/sdio8997_uart_combo.bin

   insmod /lib/modules/nxp/sdio/mlan.ko
   insmod /lib/modules/nxp/sdio/sd8xxx.ko cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=${fw}

   #insmod /lib/modules/nxp/pcie/hci_uart.ko drv_mode=1
   #hciattach /dev/ttymxc2 any 115200 flow

   btattach -B /dev/ttymxc2 -P h4 -S 115200 &
   ;;

*sdio-sdio*)
   [ "${1}" == lrd ] && fwName sdio_sdio || fw=nxp/sdio8997_sdio_combo.bin

   insmod /lib/modules/nxp/sdio/mlan.ko
   insmod /lib/modules/nxp/sdio/sd8xxx.ko cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=${fw}

   #insmod /lib/modules/nxp/sdio/bt8xxx.ko

   modprobe btmrvl_sdio
   ;;

*pcie-uart*)
   [ "${1}" == lrd ] && fwName pcie_uart || fw=nxp/pcie8997_uart_combo.bin

   insmod /lib/modules/nxp/pcie/mlan.ko
   insmod /lib/modules/nxp/pcie/pcie8xxx.ko cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=${fw}

   #insmod /lib/modules/nxp/pcie/hci_uart.ko drv_mode=1
   #hciattach /dev/ttymxc2 any 115200 flow

   btattach -B /dev/ttymxc2 -P h4 -S 115200 &
   ;;
esac

sleep 2

nmcli device set mlan0 managed no
killall sdcsupp
