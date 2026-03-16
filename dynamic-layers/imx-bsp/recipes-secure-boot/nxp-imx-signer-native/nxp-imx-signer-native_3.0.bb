SUMMARY = "NXP IMX Signer"
DESCRIPTION = "Image signing automation tool using CST/SPSDK"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1f6f1c0be32491a0c8d2915607a28f36"

SRC_URI = "git://github.com/nxp-imx-support/nxp-imx-signer.git;protocol=https;nobranch=1"
SRCREV = "b8807075433527044b19f02f29f40fe9aa10220f"

inherit native

S = "${WORKDIR}/git"

do_install () {
    install -D -m 0755 -t "${D}${bindir}/" "${S}/src/imx_signer"

    install -D -m 0644 -t "${D}${datadir}/" \
        "${S}/csf_hab4.cfg.sample" \
        "${S}/csf_hab4_pkcs11.cfg.sample" \
        "${S}/spsdk_ahab.yaml.sample"
}
