#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (c) 2026 Ezurio LLC.

set -u

failed=0

while read -r name value; do
    if ! gpioset -t0 "${name}=${value}" >/dev/null 2>&1; then
        echo "gpio-helper-init: failed to set ${name}=${value}" >&2
        failed=1
    fi
done << 'EOF'
bt_nautorun 0
bt_reset 0
ser_rs485_hd 1
ser_nrs232 0
ser_term 0
ser_nreset 1
lte_power 0
lte_on 0
lte_reset 0
lte_airplane 0
lte_wakeup 0
EOF

exit "${failed}"
