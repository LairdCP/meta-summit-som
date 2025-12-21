FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:imx95-nitrogen = "\
     file://NITROGEN_IMX95_LPDDR5_6400MTS_timing.c \
     "

python do_patch:append:imx95-nitrogen () {
    bb.utils.copyfile(
        d.expand("${UNPACKDIR}/NITROGEN_IMX95_LPDDR5_6400MTS_timing.c"), 
        d.expand("${S}/boards/mx95lp5/ddr/NITROGEN_IMX95_LPDDR5_6400MTS_timing.c"), 
        True)
}
