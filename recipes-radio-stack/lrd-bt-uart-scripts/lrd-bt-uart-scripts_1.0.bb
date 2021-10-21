SUMMARY = "Laird Connectivity Bluetooth Configurations"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch systemd

SRC_URI = " \
    file://bt-service.sh \
    file://80-btattach.rules \
    file://btattach.service \
    "

S = "${WORKDIR}"

FILES_${PN} += "${systemd_unitdir}/system ${sysconfdir}"

SYSTEMD_SERVICE_${PN} = "btattach.service"

do_install() {
    install -D -m 0775 ${S}/bt-service.sh \
        ${D}${bindir}/bt-service.sh
    install -D -m 0644 ${S}/80-btattach.rules \
        ${D}${sysconfdir}/udev/rules.d/80-btattach.rules
    install -D -m 0644 ${S}/btattach.service \
        ${D}${systemd_unitdir}/system/btattach.service
}
