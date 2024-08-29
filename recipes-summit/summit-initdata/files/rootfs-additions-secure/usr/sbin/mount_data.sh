#!/bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

set -e

DATA_MOUNT=/data

case "${1}" in
start)
	# shellcheck source=/dev/null
	. /usr/sbin/boot-rootfs.sh

	DATA_DEVICE=/dev/$(getPart rootfs_data)
	DATA_SIZE=$(/usr/bin/lsblk -ndbo SIZE "${DATA_DEVICE}")

	if [ -f /perm/caam/datakey ]; then
		caam-keygen import /perm/caam/datakey.bb datakey
	else
		caam-keygen create datakey ecb -s 16
	fi
	/usr/bin/keyctl padd logon logkey: @s < /perm/caam/datakey

	/usr/sbin/dmsetup -v create data_enc --table "0 $((DATA_SIZE / 512)) \
		crypt capi:tk(cbc(aes))-plain :36:logon:logkey: 0 ${DATA_DEVICE} \
		0 1 sector_size:512"

	[ "$(/usr/bin/lsblk -ndo FSTYPE /dev/mapper/data_enc)" = "ext4" ] || \
		/usr/sbin/mkfs.ext4 /dev/mapper/data_enc

	/usr/bin/mount -o noatime,noexec,nosuid,nodev -t auto \
		/dev/mapper/data_enc ${DATA_MOUNT} || {
		/usr/sbin/dmsetup remove data_enc
		die "Mounting ${DATA_DEVICE} to ${DATA_MOUNT} Failed"
	}

	# Create encrypted data directory
	DATA_SECRET=${DATA_MOUNT}/secret
	mkdir -p ${DATA_SECRET}

	/usr/sbin/do_factory_reset.sh check || {
		/usr/bin/umount ${DATA_MOUNT}
		/usr/sbin/dmsetup remove data_enc
		echo 3 >/proc/sys/vm/drop_caches
	}

	echo "Secure Boot Cycle Complete" >/dev/console
	;;

stop)
	/usr/bin/umount ${DATA_MOUNT}
	/usr/sbin/dmsetup remove data_enc
	echo 3 >/proc/sys/vm/drop_caches
	;;

*)
	echo "Usage: ${0} <start|stop>"
	exit 1
	;;
esac
