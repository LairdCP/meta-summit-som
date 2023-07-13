
UBOOT_MKIMAGE ?= "uboot-mkimage"
UBOOT_MKIMAGE_SIGN ?= "${UBOOT_MKIMAGE}"

UBOOT_SIGN_ENABLE ?= "0"
FIT_SIGN_INDIVIDUAL ?= "0"

FIT_HASH_ALG ?= "sha256"
FIT_SIGN_ALG ?= "rsa2048"
FIT_PAD_ALG ?= "pkcs-1.5"

UBOOT_ENCRYPT_ENABLE ?= "0"
FIT_CRYPT_ALG ?= "aes128"

DEPENDS += "u-boot-tools-native dtc-native"

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
	firmware_padding_algo="${FIT_PAD_ALG}"

        if [ "${UBOOT_SIGN_ENABLE}" = "1" -a "${FIT_SIGN_INDIVIDUAL}" = "1" ]; then
                firmware_sign_keyname="${UBOOT_SIGN_IMG_KEYNAME}"
        else
                firmware_sign_keyname=""
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

	if [ "${UBOOT_ENCRYPT_ENABLE}" = "1" ]; then
		cat << EOF >> $1
			cipher {
				algo = "${FIT_ENCRYPT_ALGO}";
				key-name-hint = "${UBOOT_ENCRYPT_KEYNAME}";
				iv-name-hint = "${UBOOT_ENCRYPT_IVNAME}";
			};
EOF
	fi

	if [ -n "$firmware_sign_keyname" ]; then
		sed -i '$ d' $1
		cat << EOF >> $1
                        signature-1 {
                                algo = "$firmware_csum,$firmware_sign_algo";
                                key-name-hint = "$firmware_sign_keyname";
				padding = "$firmware_padding_algo";
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
	scr_padding_algo="${FIT_PAD_ALG}"

        if [ "${UBOOT_SIGN_ENABLE}" = "1" -a "${FIT_SIGN_INDIVIDUAL}" = "1" ]; then
                scr_sign_keyname="${UBOOT_SIGN_IMG_KEYNAME}"
        else
                scr_sign_keyname=""
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

	if [ "${UBOOT_ENCRYPT_ENABLE}" = "1" ]; then
		cat << EOF >> $1
			cipher {
				algo = "${FIT_ENCRYPT_ALGO}";
				key-name-hint = "${UBOOT_ENCRYPT_KEYNAME}";
				iv-name-hint = "${UBOOT_ENCRYPT_IVNAME}";
			};
EOF
	fi

	if [ -n "$scr_sign_keyname" ]; then
		sed -i '$ d' $1
		cat << EOF >> $1
                        signature-1 {
                                algo = "$scr_csum,$scr_sign_algo";
                                key-name-hint = "$scr_sign_keyname";
				padding = "$scr_padding_algo";
                        };
                };
EOF
	fi
}

#
# Emit the fitImage ITS configuration section
#
# $1 ... .its filename
# $2 ... images ID
fitimage_emit_section_config() {

	conf_csum="${FIT_HASH_ALG}"
	conf_sign_algo="${FIT_SIGN_ALG}"
	conf_padding_algo="${FIT_PAD_ALG}"

        if [ "${UBOOT_SIGN_ENABLE}" = "1"  ]; then
		conf_sign_keyname="${UBOOT_SIGN_KEYNAME}"
	fi

	its_file="${1}"
	images="${2}"

	cat << EOF >> ${its_file}
                default = "config-1";

                config-1 {
			description = "Default";
			loadables = "${images}";
EOF

	if [ -n "${conf_sign_keyname}" ] ; then
		cat << EOF >> ${its_file}
                        signature-1 {
                                algo = "${conf_csum},${conf_sign_algo}";
                                key-name-hint = "${conf_sign_keyname}";
				padding = "$conf_padding_algo";
				sign-images = "loadables";
                        };
EOF
	fi

	cat << EOF >> ${its_file}
                };
EOF
}

fitimage_firmware() {
	count=1

	rm -rf $1

	fitimage_emit_fit_header $1
	fitimage_emit_section_maint $1 imagestart
	fitimage_emit_section_firmware $1 $count $2 "none"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 confstart
	fitimage_emit_section_config $1 "firmware-1"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 fitend

	${UBOOT_MKIMAGE} -f $1 $3

        if [ "${UBOOT_SIGN_ENABLE}" = "1" ]; then
		${UBOOT_MKIMAGE_SIGN} -F -k "${UBOOT_SIGN_KEYDIR}" ${3}
	fi
}

fitimage_script() {
	count=1

	rm -rf $1

	fitimage_emit_fit_header $1
	fitimage_emit_section_maint $1 imagestart
	fitimage_emit_section_script $1 $count $2 "none"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 confstart
	fitimage_emit_section_config $1 "script-1"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 fitend

	${UBOOT_MKIMAGE} -f $1 $3

        if [ "${UBOOT_SIGN_ENABLE}" = "1" ]; then
		${UBOOT_MKIMAGE_SIGN} -F -k "${UBOOT_SIGN_KEYDIR}" ${3}
	fi
}
