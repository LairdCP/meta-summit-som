#!/bin/sh

set -e

DATA_MOUNT=/data

case "${1}" in
start)
	# Mount proper data device (based on boot side)
	read -r cmdline </proc/cmdline
	for x in ${cmdline}; do
		case "$x" in
		/dev/mmcblk*)
			ROOTDEV=${x}
			break
			;;
		esac
	done

	PART=${ROOTDEV#*p}
	DRV=${ROOTDEV%p*}

	DATA_DEVICE=${DRV}p$((PART + 1))

	if [ -f /perm/caam/datakey ]; then
		caam-keygen import /perm/caam/datakey.bb datakey
	else
		caam-keygen create datakey ecb -s 16
	fi
	cat /perm/caam/datakey | keyctl padd logon logkey: @s

	dmsetup -v create data_enc --table "0 $(($(lsblk -nbo SIZE ${DATA_DEVICE}) / 512)) crypt capi:tk(cbc(aes))-plain :36:logon:logkey: 0 ${DATA_DEVICE} 0 1 sector_size:512"

	[ "$(lsblk -no FSTYPE /dev/mapper/data_enc)" = "ext4" ] || mkfs.ext4 /dev/mapper/data_enc

	/usr/bin/mount -o noatime,noexec,nosuid,nodev -t auto /dev/mapper/data_enc ${DATA_MOUNT}

	. do_factory_reset.sh check

	echo "Secure Boot Cycle Complete" >/dev/console
	;;

stop)
	/usr/bin/umount ${DATA_MOUNT}
	dmsetup remove data_enc
	echo 3 >/proc/sys/vm/drop_caches
	;;

*)
	echo "Usage: ${0} <start/stop>"
	exit 1
	;;
esac
