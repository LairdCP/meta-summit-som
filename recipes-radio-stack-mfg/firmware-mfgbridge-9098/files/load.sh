#!/bin/sh

echo "Loading Drivers"
echo "This firmware checks the software compatability byte"

modprobe moal cal_data_cfg=none mfg_mode=1 drv_mode=1 fw_name=nxp/pcieuart9098_combo.bin
