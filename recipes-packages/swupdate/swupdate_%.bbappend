FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "\
        file://1001-run-shell-scripts-indirectly.patch \
        file://1002-ubiattach.patch \
        file://1003-ubi-mtd-name.patch \
        file://1004-hawkbit-config-timeout.patch \
        file://1005-install-staging.patch \
        file://1006-enable-optional-https-certificate-check.patch \
        file://1007-flash-status.patch \
        file://1008-atmel-header-update.patch \
        file://1009-do-not-force-hash-check-if-disabled.patch \
        file://1010-printf-format.patch \
        file://1011-ignore-image-bad-partition.patch \
        file://1012-fix-update-premature-abort.patch \
        file://1013-fix-version-error-message.patch \
        file://1015-ubi-skip-crc-check.patch \
        file://1016-fat-format.patch \
        file://1020-hw-compatibility.patch \
        file://1022-fix-offset-type.patch \
        "

SYSTEMD_SERVICE:${PN}:summitsom = "swupdate.socket"
