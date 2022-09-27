echo "Unloading lrdmwl Drivers"

dtb=$(fw_printenv -n conf)

killall btattach 2> /dev/null

case "${dtb}" in
*sdio-uart*)
   modprobe -r lrdmwl_sdio lrdmwl hci_uart
   rm -f /lib/firmware/lrdmwl/88W8997_sdio_mfg.bin
   ;;

*sdio-sdio*)
   modprobe -r lrdmwl_sdio lrdmwl btmrvl_sdio
   rm -f /lib/firmware/lrdmwl/88W8997_sdio_mfg.bin
   ;;

*pcie-uart*)
   modprobe -r lrdmwl_pcie lrdmwl hci_uart
   rm -f /lib/firmware/lrdmwl/88W8997_pcie_mfg.bin
   ;;
esac
