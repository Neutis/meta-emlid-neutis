DESCRIPTION = "Service for ap6212 bluetooth initialization"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "\
    file://ap6212-bt-init.service \
    file://ap6212-bt-init \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SYSTEMD_SERVICE_${PN} = "ap6212-bt-init.service"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_RESTART = "false"

RDEPENDS_${PN} = "systemd"
DEPENDS = "systemd"

inherit systemd

S = "${WORKDIR}"

do_install() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/ap6212-bt-init.service ${D}${systemd_unitdir}/system

    install -d ${D}${bindir}
    install -c -m 0755 ${B}/ap6212-bt-init ${D}${bindir}
}

systemd_postinst() {
OPTS=""

if [ -n "$D" ]; then
    OPTS="--root=$D"
fi

if type systemctl >/dev/null 2>/dev/null; then
    systemctl $OPTS ${SYSTEMD_AUTO_ENABLE} ${SYSTEMD_SERVICE}

    if [ -z "$D" -a "${SYSTEMD_AUTO_RESTART}" = "true" ]; then
        systemctl restart ${SYSTEMD_SERVICE}
    fi
fi
}

FILES_${PN} += "${base_libdir}/systemd/system/ap6212-bt-init.service"
FILES_${PN} += "${bindir}/ap6212-bt-init"
