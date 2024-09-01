inherit custom-fit-gen

verity_setup() {
    local INPUT=${IMAGE_NAME}.${1}
    local OUTPUT=${INPUT}.verity
    local SIZE=$(stat --printf="%s" ${INPUT})

    cp -a ${INPUT} ${OUTPUT} || return 1

    # Let's drop the first line of output (doesn't contain any useful info)
    # and feed the rest to another function.
    veritysetup --hash-offset=${SIZE} format ${OUTPUT} ${OUTPUT} | \
        sed -r '1d; s/^([^:]+):\s+(.+)/\U\1=\E\2/; s/ /_/g' > ${OUTPUT}.env

    ln -sf ${OUTPUT}.env ${IMAGE_LINK_NAME}.${1}.verity.env

    fallocate -d ${OUTPUT}

    verity_boot_script
}

verity_boot_script() {
    set -x
    local env=${DM_VERITY_IMAGE_FNAME}.env
    local scr=${IMAGE_NAME}.${DM_VERITY_IMAGE_TYPE}.verity.scr
    local scrl=${DM_VERITY_IMAGE_FNAME}.scr
    local img_type=${DM_VERITY_IMAGE_TYPE}

    while read -r line; do eval ${line}; done < ${env}

    # Add partition size
    local HASH_BLOCK=$(expr ${DATA_BLOCKS} + 1)
    local DATA_SECT=$(expr ${DATA_BLOCKS} \* ${DATA_BLOCK_SIZE} / 512)
    #local BOOT_DEV='/dev/mmcblk${mmcdev}p${rootvol}'

    {
        printf 'dm_table="vroot,%s,,ro,0 %s verity 1 ${boot_dev} ${boot_dev} %s %s %s %s %s %s %s"\n' \
            ${UUID} ${DATA_SECT} ${DATA_BLOCK_SIZE} ${HASH_BLOCK_SIZE} \
            ${DATA_BLOCKS} ${HASH_BLOCK} ${HASH_ALGORITHM} ${ROOT_HASH} ${SALT}
        printf 'setenv bootargs "${bootargs} dm-mod.create=\"${dm_table}\" '
        printf 'dm-mod.waitfor=${boot_dev} root=/dev/dm-0 rootwait rootfstype=%s ro"\n' \
            ${img_type%%-*}
    } > ${scr}

    fitimage_script ${scr}.its ${scr} ${scr}.bin

    ln -sf ${scr}.bin ${scrl}.bin
    ln -sf ${scr}.bin fitImageVerity.bin
}

IMAGE_TYPES += "verity"
CONVERSIONTYPES += "verity"
CONVERSION_CMD:verity = "verity_setup ${type}"
CONVERSION_DEPENDS_verity = "cryptsetup-native"

python __anonymous() {
    image_fstypes = d.getVar('IMAGE_FSTYPES')
    pn = d.getVar('PN')

    if "wic" not in image_fstypes:
        return

    fstypes_list = image_fstypes.split()

    # If we're using wic: we'll have to use partition images and not the rootfs
    # source plugin so add the appropriate dependency.
    for fst in fstypes_list:
        if ".verity" in fst:
            f = fst[:fst.index(".verity")]
            dep = ' %s:do_image_%s' % (pn, f.replace('-', '_'))
            d.appendVarFlag('do_image_wic', 'depends', dep)
            d.setVar('DM_VERITY_IMAGE_TYPE', f)
            link_name=d.getVar('IMAGE_LINK_NAME')
            d.setVar('DM_VERITY_IMAGE_FNAME', '%s.%s' % (link_name, fst))
}
