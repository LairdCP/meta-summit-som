#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

set -o pipefail

[ "${1}" = b ] && part=2 || part=1

timeout=10
while [ ${timeout} -gt 0 ]; do
    sync
    mmc bootpart enable "${part}" 1 "${2}" >&2
    mmc extcsd read "${2}" | grep -q "Boot Partition ${part} enabled" && exit 0
    sleep 1
    timeout=$((timeout - 1))
done

exit 1
