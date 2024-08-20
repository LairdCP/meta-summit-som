#!/bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

DATA_DEV_SRC=${1}
DATA_DEV_TGT=${2}
MOUNT_POINT=/tmp/transfer_mount_point

DATA_SRC=/data

die() {
	echo "${1}" >&2
	exit 1
}

warning() {
	if [ -x /usr/bin/systemd-cat ]; then
		echo "${1}" | systemd-cat -t "${0}" -p warning
	else
		echo "${1}" >&2
	fi
}

cleanup() {
	if [ -d "${MOUNT_POINT}" ]; then
		/bin/umount ${MOUNT_POINT} || true
		dmsetup remove data_enc_o
		rmdir ${MOUNT_POINT}
	fi
}

migrate_data() {
	[ -f /perm/caam/datakey ] && [ -n "${1}" ] || return

	caam-keygen import /perm/caam/datakey.bb datakey
	keyctl padd logon logkey: @s < /perm/caam/datakey

	dmsetup -v create data_enc_o --table "0 $(($(lsblk -nbo SIZE "${1}") / 512)) crypt capi:tk(cbc(aes))-plain :36:logon:logkey: 0 ${1} 0 1 sector_size:512" ||\
		die "dm_crypt table creation for ${1} Failed"

	# Wipe data patition
	mkfs.ext4 /dev/mapper/data_enc_o

	mkdir -p "${MOUNT_POINT}" || exit_on_error false "Directory Creation for ${MOUNT_POINT} Failed"

	# Create mount point and mount the data device
	/bin/mount -o noatime,noexec,nosuid,nodev -t auto /dev/mapper/data_enc_o ${MOUNT_POINT} ||
		die "Mounting ${1} to ${MOUNT_POINT} Failed"


	cp -fa -t ${MOUNT_POINT} ${DATA_SRC}/* ||
		die "Data Copying.. Failed"

	sync

	# Unmount the data device
	/bin/umount "${MOUNT_POINT}" || die "Unmounting ${MOUNT_POINT} Failed"
	dmsetup remove data_enc_o
	rmdir "${MOUNT_POINT}"
}

# Find location for /data
DATA_MOUNT=$(awk "\$2 == \"${DATA_SRC}\" { print \$1 }" /proc/mounts)
[ ! -L "${DATA_MOUNT}" ] || DATA_MOUNT=$(readlink -f "${DATA_MOUNT}")

# Migrate only from secure partitions
case "${DATA_MOUNT}" in
	/dev/dm-*)
		DATA_MOUNT=$(ls "/sys/class/block/${DATA_MOUNT#/dev/}/slaves")
		;;
	*)
		warning "Data from ${DATA_SRC} not migrated, because it was not mounted."
		exit 0
		;;
esac

# Migrate if /data is mounted on the expected target
if [ "${DATA_MOUNT}" = "${DATA_DEV_SRC}" ]; then
	migrate_data "${DATA_DEV_TGT}"
else
	echo "Data from ${DATA_SRC} not migrated, because it was not mounted." | systemd-cat -t "${0}" -p warning
fi
