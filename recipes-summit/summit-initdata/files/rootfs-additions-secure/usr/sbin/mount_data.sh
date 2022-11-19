#!/bin/sh

set -e

DATA_MOUNT=/data

case $1 in
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
	/bin/systemd-mount -o noatime,noexec,nosuid,nodev --fsck=no -t auto ${DATA_DEVICE} ${DATA_MOUNT}

	# Create encrypted data directory
	DATA_SECRET=${DATA_MOUNT}/secret
	mkdir -p ${DATA_SECRET}

	#FSCRYPT_KEY=ffffffffffffffff
	#keyctl search %:_builtin_fs_keys logon fscrypt:${FSCRYPT_KEY} @us
	#fscryptctl set_policy ${FSCRYPT_KEY} ${DATA_SECRET} >/dev/null

	. do_factory_reset.sh check

	echo "Secure Boot Cycle Complete" >/dev/console
	;;

stop)
	/bin/systemd-umount ${DATA_MOUNT}
	echo 3 >/proc/sys/vm/drop_caches
	;;
esac
