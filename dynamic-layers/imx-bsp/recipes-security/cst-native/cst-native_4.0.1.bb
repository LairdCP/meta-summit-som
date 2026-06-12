SUMMARY = "NXP Code Signing Tool (CST)"
DESCRIPTION = "Prebuilt NXP i.MX Code Signing Tool (CST), supporting HABv4 and \
AHAB image signing. Fetched as a release tarball and extracted for use by other \
build operations (e.g. nxp-imx-signer / binman)."
HOMEPAGE = "https://www.nxp.com/webapp/Download?colCode=IMX_CST_TOOL_NEW"

# Per NXP's Software Content Register, CST's outgoing license is BSD-3-Clause.
# The prebuilt binaries bundle several components with their own licenses.
LICENSE = "BSD-3-Clause & Apache-2.0 & MIT & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=dc56c17219895403ffc9aea66e228c8c \
    file://licenses/LICENSE.openssl;md5=3441526b1df5cc01d812c7dfc218cea6 \
    file://licenses/LICENSE.oqsprovider;md5=ab9b4308908ace39992d3080dd26824a \
    file://licenses/LICENSE.liboqs;md5=4b93ef2da47496727a4e8a59f443844e \
    file://licenses/LICENSE.json-c;md5=de54b60fbbc35123ba193fea8ee216f2 \
    file://licenses/LICENSE.libp11;md5=fad9b3332be894bab9bc501572864b29 \
    file://licenses/LICENSE.hidapi;md5=6378e0b956311ba1f71ea2cc29d00484 \
    file://licenses/LICENSE.libusb;md5=fbc093901857fcd118f065f900982c24 \
"

CST_URI ?= ""

SRC_URI = "${CST_URI}"
SRC_URI:summit-internal = "https://${RFPROS_FILESHARE_AUTH}files.devops.rfpros.com/tools/nxp/cst/${PV}/IMX_CST_TOOL_NEW.tgz"
SRC_URI[sha256sum] = "60ffc243daa5e4e2ccfac8a9b74aec6d21446122a453fcb896dc52881d2a779d"

python () {
    if not d.getVar('SRC_URI').strip():
        raise bb.parse.SkipRecipe(
            "No CST source configured: SRC_URI is empty. Set CST_URI to a CST "
            "tarball URL."
        )
}

inherit native

# The release tarball extracts to a top-level "cst-${PV}" directory.
S = "${WORKDIR}/cst-${PV}"

# Prebuilt binaries: nothing to configure or compile.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    # Install the tools directly on PATH (used by binman's 'cst' bintool and by
    # recipes that DEPEND on cst-native).
    install -D -m 0755 -t ${D}${bindir} "${S}/linux64/bin/cst" "${S}/linux64/bin/srktool"
}
