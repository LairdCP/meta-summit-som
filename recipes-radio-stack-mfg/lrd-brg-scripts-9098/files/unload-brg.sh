#!/bin/sh

echo "Unloading Mfg Bridge Drivers"

killall btattach

sleep 1

killall bluetoothd

sleep 1

modprobe -r moal hci_uart
