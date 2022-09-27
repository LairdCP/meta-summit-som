inherit custom-fit-gen

process_verity() {
    # Each line contains a key and a value string delimited by ':'. Read the
    # two parts into separate variables and process them separately. For the
    # key part: convert the names to upper case and replace spaces with
    # underscores to create correct shell variable names. For the value part:
    # just trim all white-spaces.
    IFS=":"
    while read KEY VAL; do
         VKEY=$(echo "$KEY" | tr '[:lower:]' '[:upper:]' | sed 's/ /_/g')
         VVAL=$(echo "$VAL" | tr -d ' \t')
         eval "$VKEY=$VVAL"
    done

    # Add partition size
    HASH_BLOCK=$(expr $DATA_BLOCKS + 1)
    DATA_SECT=$(expr $DATA_BLOCKS \* 8)
    BOOT_DEV='/dev/mmcblk${mmcdev}p${rootvol}'

    echo "DATA_BLOCKS=$DATA_BLOCKS"
    echo "SIZE=$SIZE"
    echo "HASH_BLOCK=$HASH_BLOCK"

    echo "dm_table=\"vroot,$UUID,,ro,0 $DATA_SECT verity 1 $BOOT_DEV $BOOT_DEV $DATA_BLOCK_SIZE $HASH_BLOCK_SIZE $DATA_BLOCKS $HASH_BLOCK $HASH_ALGORITHM $ROOT_HASH $SALT\"" > ${ENV}
    echo 'setenv bootargs "${bootargs} dm-mod.create=\"${dm_table}\" dm_verity.dev_wait=1 root=/dev/dm-0 rootwait rootfstype=squashfs ro"' >> ${ENV}
}

verity_setup() {
    local TYPE=$1
    local INPUT=${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.$TYPE
    local SIZE=$(stat --printf="%s" $INPUT)
    local OUTPUT=$INPUT.verity
    local ENV=$OUTPUT.env
    local ENVL=${IMAGE_LINK_NAME}.$TYPE.verity.env

    cp -a $INPUT $OUTPUT

    # Let's drop the first line of output (doesn't contain any useful info)
    # and feed the rest to another function.
    veritysetup --hash-offset=$SIZE format $OUTPUT $OUTPUT | tail -n +2 | process_verity

    fitimage_script $ENV.its $ENV $ENV.bin

    ln -rsf $ENV.bin $ENVL.bin
    ln -rsf $ENV.its $ENVL.its
    ln -rsf $ENV $ENVL
    install -D -t ${DEPLOY_DIR_IMAGE}/verity -m 644 $ENVL.bin
    ln -rsf ${DEPLOY_DIR_IMAGE}/verity/$ENVL.bin ${DEPLOY_DIR_IMAGE}/fitImageVerity.bin
}

IMAGE_TYPES += "verity"
CONVERSIONTYPES += "verity"
CONVERSION_CMD_verity = "verity_setup ${type}"
CONVERSION_DEPENDS_verity = "cryptsetup-native"

IMAGE_BOOT_FILES += "fitImageVerity.bin"

python __anonymous() {
    image_fstypes = d.getVar('IMAGE_FSTYPES')
    pn = d.getVar('PN')

    if "wic" not in image_fstypes:
        return

    fstypes_list = image_fstypes.split()

    for fst in fstypes_list:
        if ".verity" in fst:
            f = fst[:fst.index(".verity")]
            dep = ' %s:do_image_%s' % (pn, f.replace('-', '_'))
            d.appendVarFlag('do_image_wic', 'depends', dep)
            d.setVar('DM_VERITY_IMAGE_TYPE', fst)
}
