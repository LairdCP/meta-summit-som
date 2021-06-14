#!/bin/sh

echo "Unloading Drivers"

dtb=$(fw_printenv -n fdt_file)

killall btattach

case "${dtb}" in
*sdio*)
    rmmod sd8xxx
    ;;

*pcie*)
    rmmod pcie8xxx
    ;;
esac

rmmod mlan
modprobe -r cfg80211 hci_uart
