#!/bin/sh

echo "Unloading Mfg Bridge Drivers"

dtb=$(fw_printenv -n fdtfile)

case "${dtb}" in
*uart*)
    #killall hciattach
    killall btattach
    ;;
esac

sleep 1

killall bluetoothd

sleep 1

case "${dtb}" in
*sdio-uart*)
    rmmod sd8xxx
    #rmmod hci_uart
    modprobe -r hci_uart
    ;;

*sdio-sdio*)
    rmmod sd8xxx
    #rmmod bt8xxx
    modprobe -r btmrvl_sdio
    ;;

*pcie-uart*)
    rmmod pcie8xxx
    #rmmod hci_uart
    modprobe -r hci_uart
    ;;
esac

rmmod mlan
modprobe -r cfg80211 bnep bluetooth
