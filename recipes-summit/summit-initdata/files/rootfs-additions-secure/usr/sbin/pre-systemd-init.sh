#!/bin/sh
# Pre-systemd init script
# This script sets up a writeable partition and mount it to
# /perm before starting systemd; this is necessary because a
# few systemd requirements (logging, and a machine-id file)
# require a writeable filesystem.

mount proc /proc -t proc
mmc=$(sed -nr 's,.*(/dev/mmcblk[0-9]+)p[0-9]+.*,\1,p' /proc/cmdline)
umount /proc

mount -t sysfs sysfs /sys
PERM_MOUNT=/perm
PERM_DEVICE=$(lsblk -nlo PARTLABEL,PATH ${mmc} | sed -n "s,^perm *\(.*\),\1,p")
umount /sys

mount -t auto -o noatime,nosuid,noexec ${PERM_DEVICE} ${PERM_MOUNT} ||\
	echo "mount failed $?"

# Make sure there is at least an empty machine-id file
# (Referenced from symlink on the rootfs)
if [ ! -f "${PERM_MOUNT}/etc/machine-id" ]; then
	mkdir -p ${PERM_MOUNT}/etc
	echo '' > ${PERM_MOUNT}/etc/machine-id
fi

mkdir -p ${PERM_MOUNT}/log/journal

# Start init
exec /usr/sbin/init
