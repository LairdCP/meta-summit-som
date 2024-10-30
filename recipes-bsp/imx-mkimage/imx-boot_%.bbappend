inherit python3native
FILESEXTRAPATHS:prepend:summit-secure-hab := "${THISDIR}/files:"

SRC_URI:append:summit-secure-hab = " \
    file://sign_bootloader.py \
    file://parse_signing_offsets.py \
    "

do_compile[depends] += "virtual/kernel:do_deploy"

IMXBOOT_TARGETS:append:summit-secure-hab = " print_fit_hab"

DEPLOY_NETWORK = "0"
DEPLOY_NETWORK:summit-secure-hab = "1"
do_deploy[network] = "${DEPLOY_NETWORK}"

DEPENDS:summit-secure-hab += "\
    ${PYTHON_PN}-requests-native \
    ${PYTHON_PN}-msal-native \
    jq-native \
    "

SIGN_LOCALLY ?= "1"

# The following variables are required to sign the bootloader remotely and are expected to be set in
# the local.conf or as environment variables (pulled in via the setup-environment script when
# sourced).
RFPROS_SIGNING_SERVER_URL ?= ""
RFPROS_SIGNING_AUTH_CLIENT_ID ?= ""
RFPROS_SIGNING_AUTH_AUTHORITY ?= ""
RFPROS_SIGNING_AUTH_CLIENT_SECRET ?= ""
RFPROS_SIGNING_AUTH_SCOPES ?= ""
RFPROS_SIGNING_PART_NUMBER ?= ""
RFPROS_SIGNING_ACCESS_TOKEN ?= ""

# The following variables are required to sign the bootloader locally and are expected to be set in
# the local.conf or as environment variables (pulled in via the setup-environment script when
# sourced). In order to locally sign the bootloader, the SIGN_LOCALLY variable must be set to 1, and
# the NXP CST tool must be present at the path specified by the CST_BIN_PATH variable and
# executable. Additionally, the necessary files listed below must be present in the CST_PATH
# directory:
#
# CST_PATH/
# ├── linux64/
# │   └── bin/
# │       └── cst
# ├── keys/
# │   ├── CSFx_usr_key.pem/.der
# │   ├── IMGx_usr_key.pem/.der
# │   ├── key_pass.txt
# │   └── SRK_x_table.bin
# ├── crts/
# │   ├── CSFx_usr_crt.pem/.der
# │   └── IMGx_usr_crt.pem/.der
# ├── csf_spl.txt.template
# └── csf_fit.txt.template
#
# The CSF template files are expected to contain a placeholder string of
# "{AUTHENTICATE_DATA_BLOCKS}" which is replaced with the actual data block information by these
# scripts.
#
# Refer to the NXP CST tool documentation for more information on the required files and their
# contents.
CST_PATH ?= ""
CST_BIN_PATH ?= "${CST_PATH}/linux64/bin/cst"
CSF_SPL_TXT ?= "${CST_PATH}/csf_spl.txt"
CSF_FIT_TXT ?= "${CST_PATH}/csf_fit.txt"
CSF_SPL_TXT_TEMPLATE ?= "${CSF_SPL_TXT}.template"
CSF_FIT_TXT_TEMPLATE ?= "${CSF_FIT_TXT}.template"

do_deploy:append:summit-secure-hab() {
    SIGNED_FLASH_BIN_PATH="${DEPLOYDIR}/${BOOT_TOOLS}/signed_flash.bin"

    signing_offsets_json=$($PYTHON "${WORKDIR}/parse_signing_offsets.py" \
        --compile-log="${WORKDIR}/temp/log.do_compile"\
        )

    if [ "${SIGN_LOCALLY}" = "1" ]; then
        echo "Signing the bootloader locally"

        # Extract the signing offsets from JSON
        spl_hab_block=$(echo "${signing_offsets_json}" | jq -r '.splHabBlock')
        spl_hab_block_addr=$(echo "${spl_hab_block}" | jq -r '.addr')
        spl_hab_block_off=$(echo "${spl_hab_block}" | jq -r '.off')
        spl_hab_block_len=$(echo "${spl_hab_block}" | jq -r '.len')
        spl_csf_off=$(echo "${signing_offsets_json}" | jq -r '.splCsfOff')
        sld_hab_block=$(echo "${signing_offsets_json}" | jq -r '.sldHabBlock')
        sld_hab_block_addr=$(echo "${sld_hab_block}" | jq -r '.addr')
        sld_hab_block_off=$(echo "${sld_hab_block}" | jq -r '.off')
        sld_hab_block_len=$(echo "${sld_hab_block}" | jq -r '.len')
        fit_csf_off=$(echo "${signing_offsets_json}" | jq -r '.fitCsfOff')
        fit_image_data_blocks=$(echo "${signing_offsets_json}" | jq -c '.fitImageDataBlocks | fromjson | .[]')

        # Print the signing offsets
        echo "${signing_offsets_json}"

        # Generate the CSF file for the SPL
        while IFS= read -r line
        do
            case "$line" in
                *"{AUTHENTICATE_DATA_BLOCKS}"*)
                    data_blocks_line=$(echo "$line" | sed "s/{AUTHENTICATE_DATA_BLOCKS}/${spl_hab_block_addr} ${spl_hab_block_off} ${spl_hab_block_len} \"flash.bin\"/")
                    csf_spl="${csf_spl}${data_blocks_line}\n"
                    ;;
                *)
                    csf_spl="$csf_spl$line\n"
                    ;;
            esac
        done < "${CSF_SPL_TXT_TEMPLATE}"
        echo "${csf_spl}" > "${CSF_SPL_TXT}"

        # Generate the CSF file for the FIT
        while IFS= read -r line
        do
            case "$line" in
                *"{AUTHENTICATE_DATA_BLOCKS}"*)
                    fit_image_blocks_str=""
                    for block in ${fit_image_data_blocks}; do
                        block_addr=$(echo "${block}" | jq -r '.addr')
                        block_off=$(echo "${block}" | jq -r '.off')
                        block_len=$(echo "${block}" | jq -r '.len')
                        fit_image_blocks_str=",\\\\\n                ${block_addr} ${block_off} ${block_len} \"flash.bin\"${fit_image_blocks_str}"
                    done
                    data_blocks_line=$(echo "$line" | sed "s/{AUTHENTICATE_DATA_BLOCKS}/${sld_hab_block_addr} ${sld_hab_block_off} ${sld_hab_block_len} \"flash.bin\"${fit_image_blocks_str}/")
                    csf_fit="${csf_fit}${data_blocks_line}\n"
                    ;;
                *)
                    csf_fit="$csf_fit$line\n"
                    ;;
            esac
        done < "${CSF_FIT_TXT_TEMPLATE}"
        echo "${csf_fit}" > "${CSF_FIT_TXT}"

        # Create a flash.bin copy in the CST_PATH directory
        cp "${BOOT_STAGING}/flash.bin" "${CST_PATH}/flash.bin"
        
        # Create SPL CSF binary file
        (cd "${CST_PATH}" && ${CST_BIN_PATH} -i "${CSF_SPL_TXT}" -o "${DEPLOYDIR}/${BOOT_TOOLS}/csf_spl.bin")

        # Create FIT CSF binary file
        (cd "${CST_PATH}" && ${CST_BIN_PATH} -i "${CSF_FIT_TXT}" -o "${DEPLOYDIR}/${BOOT_TOOLS}/csf_fit.bin")

        # Create a flash.bin copy to sign
        cp "${BOOT_STAGING}/flash.bin" "${SIGNED_FLASH_BIN_PATH}"

        # Insert SPL CSF binary file into output file
        spl_csf_off=$(printf "%d" "$spl_csf_off")
        dd if="${DEPLOYDIR}/${BOOT_TOOLS}/csf_spl.bin" of="${SIGNED_FLASH_BIN_PATH}" bs=1 seek="${spl_csf_off}" conv=notrunc

        # Insert FIT CSF binary file into output file
        fit_csf_off=$(printf "%d" "$fit_csf_off")
        dd if="${DEPLOYDIR}/${BOOT_TOOLS}/csf_fit.bin" of="${SIGNED_FLASH_BIN_PATH}" bs=1 seek="${fit_csf_off}" conv=notrunc
    else
        echo "Signing the bootloader remotely"

        # Call the sign_bootloader.py script to sign the bootloader
        $PYTHON "${WORKDIR}/sign_bootloader.py" \
            --server-url="${RFPROS_SIGNING_SERVER_URL}" \
            --part-number="${RFPROS_SIGNING_PART_NUMBER}" \
            --signing-offsets="${signing_offsets_json}" \
            --unsigned-flash="${BOOT_STAGING}/flash.bin" \
            --signed-flash="${SIGNED_FLASH_BIN_PATH}" \
            --access-token="${RFPROS_SIGNING_ACCESS_TOKEN}" \
            --auth-client-id="${RFPROS_SIGNING_AUTH_CLIENT_ID}" \
            --auth-authority="${RFPROS_SIGNING_AUTH_AUTHORITY}" \
            --auth-client-secret="${RFPROS_SIGNING_AUTH_CLIENT_SECRET}" \
            --auth-scopes="${RFPROS_SIGNING_AUTH_SCOPES}"
    fi
        
    # Update the imx-boot symlink to point to the signed bootloader
    ln -sf "${BOOT_TOOLS}/signed_flash.bin" "${DEPLOYDIR}/${BOOT_NAME}"
}
