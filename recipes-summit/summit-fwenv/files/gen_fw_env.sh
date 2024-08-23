#! /bin/sh
# shellcheck disable=SC2154
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

# shellcheck source=/dev/null
. /usr/sbin/boot-rootfs.sh

if [ "${rootDevType}" != "MMC" ]; then
	mmcenv=
else
	getSide
	if [ "${bootside}" = b ]; then
		mmcenv=boot1
	else
		mmcenv=boot0
	fi
fi

echo "/dev/${rootDevActual%%p*}${mmcenv} 0x3f0000 0x4000" > /run/fw_env.config
