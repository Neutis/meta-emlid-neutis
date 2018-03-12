DESCRIPTION = "Post install script for Emlid Neutis"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "\
    file://post-install.service \
    file://post-install.sh \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SYSTEMD_SERVICE_${PN} = "post-install.service"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_RESTART = "false"

RDEPENDS_${PN} = "systemd"
DEPENDS = "systemd"

inherit systemd

S = "${WORKDIR}"

do_install() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/post-install.service ${D}${systemd_unitdir}/system

    install -d ${D}${base_sbindir}
    install -c -m 0755 ${B}/post-install.sh ${D}${base_sbindir}
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

FILES_${PN} += "${base_libdir}/systemd/system/post-install.service"
FILES_${PN} += "${base_sbindir}/post-install.sh"
