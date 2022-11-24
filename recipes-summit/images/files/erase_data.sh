#!/bin/sh

MOUNT_POINT=/tmp/transfer_mount_point
DATA_SECRET_SRC=/data/secret
DATA_SECRET_TARGET=${MOUNT_POINT}/secret
DATA_SRC=/data
DATA_TARGET=${MOUNT_POINT}

exit_on_error() {
	[ "${1}" = 1 ] && /bin/umount "${MOUNT_POINT}"
	echo "${2}"
	exit 1
}

migrate_data() {
	# Create mount point and mount the data device
	/bin/mount -o noatime,noexec,nosuid,nodev -t auto "${1}" "${MOUNT_POINT}" ||
		exit_on_error 0 "Mounting ${DATA_DEVICE} to ${MOUNT_POINT} Failed"

	# Wipe data patition
	rm -rf ${MOUNT_POINT}/*

	if ${do_data_migration}; then

		# Prepare /data/secret
		if [ -d "${DATA_SECRET_SRC}" ]; then
			mkdir -p "${DATA_SECRET_TARGET}"

			# Needs keyring to access secret data.
			#/bin/keyctl link @us @s
			# Target dir is not encrypted anymore after nand erase. Encrypt it before migrating data.
			#FSCRYPT_KEY=ffffffffffffffff
			#/bin/fscryptctl set_policy ${FSCRYPT_KEY} ${DATA_SECRET_TARGET} ||
			#	exit_on_error 1 "Directory Encryption.. Failed"
		fi

		cp -fa -t ${DATA_TARGET}/ ${DATA_SRC}/* ||
			exit_on_error 1 "Data Copying.. Failed"
	fi

	sync

	# Unmount the data device
	/bin/umount "${MOUNT_POINT}" || exit_on_error 0 "Unmounting ${MOUNT_POINT} Failed"
}

mkdir -p "${MOUNT_POINT}" || exit_on_error 0 "Directory Creation for ${MOUNT_POINT} Failed"

mmc=$(sed -nr 's,.*/dev/(mmcblk[0-9]+)p[0-9]+.*,\1,p' /proc/cmdline)

read -r mmctype < /sys/block/${mmc}/device/type
[ "${mmctype}" = "SD" ] && run_on_sd=true || run_on_sd=false

# Don't migrate data from SD and if /data not mounted
if ! run_on_sd && grep -qs "${DATA_SRC} " /proc/mounts; then
	do_data_migration=true
else
	echo "Data from ${DATA_SRC} not migrated, because it was not mounted." | systemd-cat -t "${0}" -p warning
	do_data_migration=false
fi

for name in ${1}; do
	other=$(lsblk -nlo PARTLABEL,PATH /dev/${mmc} | sed -n "s,^${name} *\(.*\),\1,p")
	migrate_data "${other}"
done

rm -rf "${MOUNT_POINT}"
