#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

# shellcheck source=/dev/null
. /usr/sbin/boot-rootfs.sh

if [ "$(rootDevType)" != "MMC" ]; then
	mmcenv=
elif [ "$(getSide)" = b ]; then 
	mmcenv=boot1
else
	mmcenv=boot0
fi

echo "/dev/${rootDevName:?}${mmcenv} 0x3f0000 0x4000" > /run/fw_env.config
