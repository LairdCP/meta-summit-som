#!/bin/sh

set -x

MOUNT_POINT=/tmp/transfer_mount_point
DATA_SRC=/data
DATA_TARGET=${MOUNT_POINT}

exit_on_error() {
	if ${1}; then 
		/bin/umount "${MOUNT_POINT}"
		dmsetup remove data_enc_o
		rm -rf "${MOUNT_POINT}"
	fi
	echo "${2}"
	exit 1
}

migrate_data() {
	[ -f /perm/caam/datakey -a -n "${1}" ] || return

	caam-keygen import /perm/caam/datakey.bb datakey
	cat /perm/caam/datakey | keyctl padd logon logkey: @s

	dmsetup -v create data_enc_o --table "0 $(($(lsblk -nbo SIZE ${1}) / 512)) crypt capi:tk(cbc(aes))-plain :36:logon:logkey: 0 ${1} 0 1 sector_size:512" ||\
		exit_on_error false "dm_crypt table creation for ${1} Failed"

	[ "$(lsblk -no FSTYPE /dev/mapper/data_enc_o)" = "ext4" ] || mkfs.ext4 /dev/mapper/data_enc_o

	mkdir -p "${MOUNT_POINT}" || exit_on_error false "Directory Creation for ${MOUNT_POINT} Failed"

	# Create mount point and mount the data device
	/bin/mount -o noatime,noexec,nosuid,nodev -t auto /dev/mapper/data_enc_o ${MOUNT_POINT} ||\
		exit_on_error false "Mounting ${1} to ${MOUNT_POINT} Failed"

	# Wipe data patition
	rm -rf ${MOUNT_POINT}/*

	cp -fa -t ${DATA_TARGET}/ ${DATA_SRC}/* ||\
		exit_on_error true "Data Copying.. Failed"

	sync

	# Unmount the data device
	/bin/umount "${MOUNT_POINT}" || exit_on_error false "Unmounting ${MOUNT_POINT} Failed"
	dmsetup remove data_enc_o
	rm -rf "${MOUNT_POINT}"
}

mmc=$(sed -nr 's,.*/dev/(mmcblk[0-9]+)p[0-9]+.*,\1,p' /proc/cmdline)
read -r mmctype < /sys/block/${mmc}/device/type

# Don't migrate data from SD and if /data not mounted
if [ "${mmctype}" = "MMC" ] && grep -qs "${DATA_SRC} " /proc/mounts; then
	other=$(lsblk -nlo PARTLABEL,PATH /dev/${mmc} | sed -n "s,^${1} *\(.*\),\1,p")
	migrate_data "${other}"
else
	echo "Data from ${DATA_SRC} not migrated, because it was not mounted." | systemd-cat -t "${0}" -p warning
fi
