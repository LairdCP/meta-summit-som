#! /bin/sh
# SPDX-License-Identifier: LicenseRef-Ezurio-Clause
# Copyright (C) 2024 Ezurio

flash_cleanup() {
    [ ! -d /sys/class/ubi/ubi0 ] || /usr/sbin/ubidetach -d 0
}

flash_format() {
    /usr/bin/erase_som_nand
    /usr/sbin/ubiformat "/dev/$(sed -rn 's/^([^:]+).*"ubi".*/\1/p' /proc/mtd)" -y
}

flash_factory_test() {
    # Mark bad blocks if found
    while IFS=' :"' read -r mtd_dev _ _ mtd_name ; do
        case ${mtd_name} in
        u-boot*)
            /usr/sbin/nandtest -m "/dev/${mtd_dev}" || \
                die "NAND test failed for ${mtd_dev} \"${mtd_name}\""

            read -r bad_blocks "/sys/class/mtd/${mtd_dev}/bad_blocks"

            case ${mtd_name} in
            *spl)
                [ "${bad_blocks}" -eq 0 ] || \
                    die "Bad blocks found in ${mtd_dev} \"${mtd_name}\""
                ;;

            *env-a)
                bad_a=${bad_blocks}
                ;;

            *env-b)
                bad_b=${bad_blocks}
                ;;
            esac
        ;;
        esac
    done < /proc/mtd

    [ "${bad_a}" -eq 0 ] || [ "${bad_b}" -eq 0 ] || \
        die "Bad blocks found in both u-boot-env areas"
}
