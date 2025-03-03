
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
	cat << EOF >> "${1}"
/dts-v1/;

/ {
        description = "${FIT_DESC}";
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
	imgend)
		cat << EOF >> $1
		};
EOF
	;;
	esac
}

#
# Emit the fitImage ITS bin section
#
# $1 ... .its filename
# $2 ... Image counter
# $3 ... Path to firmware image
# $4 ... compression
# $5 ... Type
# $6 ... Description
fitimage_emit_section_bin() {

	cat << EOF >> $1
		$5-$2 {
			description = "$6";
			data = /incbin/("$3");
			type = "$5";
			compression = "$4";

			hash-1 {
				algo = "${FIT_HASH_ALG}";
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

	if [ "${UBOOT_SIGN_ENABLE}" = "1" ] && [ "${FIT_SIGN_INDIVIDUAL}" = "1" ]; then
		cat << EOF >> $1

			signature-1 {
				algo = "${FIT_HASH_ALG},${FIT_SIGN_ALG}";
				key-name-hint = "${UBOOT_SIGN_IMG_KEYNAME}";
				padding = "${FIT_PAD_ALG}";
			};
EOF
	fi

	fitimage_emit_section_maint $1 imgend
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

	its_file=${1}
	images=${2}
	node=${3}

	cat << EOF >> ${its_file}
		default = "config-1";

		config-1 {
			description = "Default";
			${node} = "${images}";
EOF

	if [ -n "${conf_sign_keyname}" ] ; then
		cat << EOF >> ${its_file}
			signature-1 {
				algo = "${conf_csum},${conf_sign_algo}";
				key-name-hint = "${conf_sign_keyname}";
				padding = "$conf_padding_algo";
				sign-images = "${node}";
			};
EOF
	fi

	fitimage_emit_section_maint $1 imgend
}

#
# Emit the fitImage ITS
#
# $1 ... .its filename
# $2 ... Path to firmware image
# $3 ... Output filename
# $4 ... Type
fitimage_bin() {
	count=1

	rm -rf $1

	case $4 in
	firmware)
		FIT_DESC="Firmware Image"
		FIT_PAYLOAD="loadables"
		;;
	script)
		FIT_DESC="Boot Script"
		FIT_PAYLOAD="script"
		;;
	esac

	fitimage_emit_fit_header $1
	fitimage_emit_section_maint $1 imagestart
	fitimage_emit_section_bin $1 $count $2 "none" $4 "${FIT_DESC}"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 confstart
	fitimage_emit_section_config $1 "$4-$count" "${FIT_PAYLOAD}"
	fitimage_emit_section_maint $1 sectend
	fitimage_emit_section_maint $1 fitend

	${UBOOT_MKIMAGE} -f $1 $3

	if [ "${UBOOT_SIGN_ENABLE}" = "1" ]; then
		${UBOOT_MKIMAGE_SIGN} -F -k "${UBOOT_SIGN_KEYDIR}" ${3}
	fi
}

fitimage_firmware() {
	fitimage_bin $1 $2 "$3" "firmware"
}

fitimage_script() {
	fitimage_bin $1 $2 "$3" "script"
}
