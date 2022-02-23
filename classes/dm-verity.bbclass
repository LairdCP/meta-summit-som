process_verity() {
    install -d ${DEPLOY_DIR}
    rm -f $1

    # Each line contains a key and a value string delimited by ':'. Read the
    # two parts into separate variables and process them separately. For the
    # key part: convert the names to upper case and replace spaces with
    # underscores to create correct shell variable names. For the value part:
    # just trim all white-spaces.
    IFS=":"
    while read KEY VAL; do
        printf '%s=%s\n' \
            "$(echo "$KEY" | tr '[:lower:]' '[:upper:]' | sed 's/ /_/g')" \
            "$(echo "$VAL" | tr -d ' \t')" >> $1
    done

    # Add partition size
    echo "DATA_SIZE=$SIZE" >> $1
}

verity_setup() {
    local TYPE=$1
    local INPUT=${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.$TYPE
    local SIZE=$(stat --printf="%s" $INPUT)
    local OUTPUT=$INPUT.verity
    local ENV="${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}.$TYPE.verity.env"

    cp -a $INPUT $OUTPUT

    # Let's drop the first line of output (doesn't contain any useful info)
    # and feed the rest to another function.
    veritysetup --hash-offset=$SIZE format $OUTPUT $OUTPUT | tail -n +2 | process_verity $ENV

    #. $ENV

    #echo dm_table="vroot,$UUID,,ro,0 $(($DATA_BLOCKS * 8)) verity 1 /dev/mmcblk1p2 /dev/mmcblk1p2 $DATA_BLOCK_SIZE $HASH_BLOCK_SIZE $DATA_BLOCKS $((DATA_BLOCKS+1)) $HASH_ALGORITHM $ROOT_HASH $SALT"
}

IMAGE_TYPES += "verity"
CONVERSIONTYPES += "verity"
CONVERSION_CMD_verity = "verity_setup ${type}"
CONVERSION_DEPENDS_verity = "cryptsetup-native"

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
