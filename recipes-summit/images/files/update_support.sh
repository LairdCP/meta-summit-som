#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

[ "${1}" = b ] && part=2 || part=1

mmc bootpart enable "${part}" 1 "${2}"
sleep 1
