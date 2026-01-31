FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:summitsom:mx8mm-generic-bsp = " \
    file://0001-imx8mm-uart4-a53.patch \
"
