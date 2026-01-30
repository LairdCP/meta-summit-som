FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:nitrogen-imx8mm = " \
    file://0001-imx8mm-uart4-a53.patch \
"
