
UBOOT_MKIMAGE ?= "uboot-mkimage"
UBOOT_MKIMAGE_SIGN ?= "${UBOOT_MKIMAGE}"

FIT_HASH_ALG ?= "sha256"
FIT_SIGN_ALG ?= "rsa2048"

DEPENDS += "u-boot-tools-native"

#
# Emit the fitImage ITS header
#
# $1 ... .its filename
fitimage_emit_fit_header() {
	cat << EOF >> $1
/dts-v1/;

/ {
        description = "${FIT_DESC}";
        #address-cells = <1>;
EOF
}

#
# Emit the fitImage section bits
#
# $1 ... .its filename
# $2 ... Section bit type: imagestart - image section start
#                          confstart  - configuration section start
#                          sectend    - section end
#                          fitend     - fitimage end
#
fitimage_emit_section_maint() {
	case $2 in
	imagestart)
		cat << EOF >> $1

        images {
EOF
	;;
	confstart)
		cat << EOF >> $1

        configurations {
EOF
	;;
	sectend)
		cat << EOF >> $1
	};
EOF
	;;
	fitend)
		cat << EOF >> $1
};
EOF
	;;
	esac
}

#
# Emit the fitImage ITS firmware section
#
# $1 ... .its filename
# $2 ... Image counter
# $3 ... Path to firmware image
# $4 ... Compression type
fitimage_emit_section_firmware() {

	firmware_csum="${FIT_HASH_ALG}"
	firmware_sign_algo="${FIT_SIGN_ALG}"

	if [ "${UBOOT_SIGN_ENABLE}" != "1" ] ; then
                firmware_sign_keyname=""
        elif [ "${FIT_SIGN_INDIVIDUAL}" = "1" ]; then
                firmware_sign_keyname="${UBOOT_SIGN_IMG_KEYNAME}"
        else
                firmware_sign_keyname="${UBOOT_SIGN_KEYNAME}"
        fi

	cat << EOF >> $1
                firmware-$2 {
                        description = "MCU firmware";
                        data = /incbin/("$3");
                        type = "firmware";
                        compression = "none";
                        hash-1 {
                                algo = "$firmware_csum";
                        };
                };
EOF

	if [ -n "$firmware_sign_keyname" ]; then
		sed -i '$ d' $1
		cat << EOF >> $1
                        signature-1 {
                                algo = "$firmware_csum,$firmware_sign_algo";
                                key-name-hint = "$firmware_sign_keyname";
                        };
                };
EOF
	fi
}

#
# Emit the fitImage ITS u-boot script section
#
# $1 ... .its filename
# $2 ... Image counter
# $3 ... Path to boot script image
fitimage_emit_section_script() {

	scr_csum="${FIT_HASH_ALG}"
	scr_sign_algo="${FIT_SIGN_ALG}"

	if [ "${UBOOT_SIGN_ENABLE}" != "1" ] ; then
                scr_sign_keyname=""
        elif [ "${FIT_SIGN_INDIVIDUAL}" = "1" ]; then
                scr_sign_keyname="${UBOOT_SIGN_IMG_KEYNAME}"
        else
                scr_sign_keyname="${UBOOT_SIGN_KEYNAME}"
        fi

        cat << EOF >> $1
                script-$2 {
                        description = "Boot script";
                        data = /incbin/("$3");
                        type = "script";
                        compression = "none";
                        hash-1 {
                                algo = "$scr_csum";
                        };
                };
EOF

	if [ -n "$scr_sign_keyname" ]; then
		sed -i '$ d' $1
		cat << EOF >> $1
                        signature-1 {
                                algo = "$scr_csum,$scr_sign_algo";
                                key-name-hint = "$scr_sign_keyname";
                        };
                };
EOF
	fi
}

fitimage_firmware() {
	count=1

	rm -rf $1

	fitimage_emit_fit_header $1
	fitimage_emit_section_maint $1 imagestart
	fitimage_emit_section_firmware $1 $count $2 "none"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 fitend

	${UBOOT_MKIMAGE} -f $1 $3
}

fitimage_script() {
	count=1

	rm -rf $1

	fitimage_emit_fit_header $1
	fitimage_emit_section_maint $1 imagestart
	fitimage_emit_section_script $1 $count $2 "none"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 fitend

	${UBOOT_MKIMAGE} -f $1 $3
}
