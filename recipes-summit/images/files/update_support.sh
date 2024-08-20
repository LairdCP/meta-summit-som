#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

[ "${1}" = b ] && part=1 || part=0

mmc bootpart enable $((part + 1)) 1 "${2}"
