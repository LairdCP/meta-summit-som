#!/bin/sh

echo "Loading Mfg Bridge Drivers"

modprobe moal cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=nxp/pcieuart9098_combo.bin

#btattach -B /dev/ttymxc1 -P h4 -S 115200 &

sleep 2

nmcli device set mlan0 managed no
killall sdcsupp
