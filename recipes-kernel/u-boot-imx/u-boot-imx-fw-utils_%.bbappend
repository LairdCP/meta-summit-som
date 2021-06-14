FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

UBOOT_SRC ?= "git://source.codeaurora.org/external/imx/uboot-imx.git;protocol=https"
SRCBRANCH = "imx_v2020.04_5.4.70_2.3.0"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH} \
"
SRCREV = "185bdaaaf5644319284566e5c340927d28954a1a"

SRC_URI += "\
	file://git \
	file://bsp-integ.patch \
	file://fw_env.config \
	"

do_install_append() {
    install -D -m 0644 ${S}/../fw_env.config ${D}${sysconfdir}/fw_env.config
}
