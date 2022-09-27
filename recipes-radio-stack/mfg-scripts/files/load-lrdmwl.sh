echo "Loading lrdmwl Drivers"

case "${1}" in
ST|SOM8MP)
  systemctl start adaptive_bt
  systemctl start adaptive_ww
  ;;
mfg)
  suffix="_mfg"
  systemctl stop adaptive_bt
  systemctl stop adaptive_ww
  ;;
*)
   echo "usage load-lrdmwl.sh [SOM8MP | ST | mfg]"
   exit 1
   ;;
esac

dtb=$(fw_printenv -n conf)

pwd=${PWD}
cd /lib/firmware/lrdmwl/

rm -f 88W8997_sdio_mfg.bin 88W8997_pcie_mfg.bin

case "${dtb}" in
*sdio-uart*)
   ln -sf 88W8997_${1}_sdio_uart_* 88W8997_sdio${suffix}.bin
   modprobe -a hci_uart lrdmwl_sdio
   ;;

*sdio-sdio*)
   ln -sf 88W8997_${1}_sdio_sdio_* 88W8997_sdio${suffix}.bin
   modprobe -a btmrvl_sdio lrdmwl_sdio
   ;;

*pcie-uart*)
   ln -sf 88W8997_${1}_pcie_uart_* 88W8997_pcie${suffix}.bin
   modprobe -a hci_uart lrdmwl_pcie
   ;;
esac

cd ${pwd}
