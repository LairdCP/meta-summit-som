#! /bin/sh

set -- $(sed -nr 's,.*/dev/(mmcblk[0-9]+)p([0-9]+).*,\1 \2,p' /proc/cmdline)
mmc="${1}"
part="${2}"

if [ -d /sys/block/${mmc}boot0 ]; then 
    if [ "${part}" -ge 4 ]; then 
        mmcenv=boot1
    else
        mmcenv=boot0
    fi
else
        mmcenv=
fi

echo "/dev/${mmc}${mmcenv} 0x3f0000 0x4000" > /run/fw_env.config
