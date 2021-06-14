#!/bin/sh

echo "Loading Drivers"

modprobe moal cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=nxp/pcieuart9098_combo.bin

#btattach -B /dev/ttymxc2 -P h4 -S 3000000 &

sleep 2

nmcli device set mlan0 managed no
killall sdcsupp
#killall bluetoothd
