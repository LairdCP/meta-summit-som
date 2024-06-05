#! /bin/sh

mmc="mmcblk2"

[ "${1}" = b ] && part=1 || part=0

mmc bootpart enable $((part + 1)) 1 /dev/${mmc}
