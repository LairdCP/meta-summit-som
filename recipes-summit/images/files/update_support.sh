#! /bin/sh

mmc="mmcblk2"

[ "$1" = b ] && part=1 || part=0

mmc bootpart enable $((part + 1)) 1 /dev/${mmc}

echo 0 > /sys/block/${mmc}boot${part}/force_ro
dd if=/dev/zero of=/dev/${mmc}boot${part} bs=512 count=32 seek=8064 conv=fsync 2>/dev/null
echo 1 > /sys/block/${mmc}boot${part}/force_ro
