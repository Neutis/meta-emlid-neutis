DESCRIPTION = "Service for reflashing, switch device to FEL mode"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "\
    file://goto-fel.c \
    file://reflash-utility.service \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SYSTEMD_SERVICE_${PN} = "reflash-utility.service"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_RESTART = "false"

RDEPENDS_${PN} = "systemd"
DEPENDS = "systemd"

inherit systemd

S = "${WORKDIR}"

do_compile() {
    ${CXX} goto-fel.c ${LDFLAGS} -o goto-fel
}

do_install() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/reflash-utility.service ${D}${systemd_unitdir}/system

    install -d ${D}${base_sbindir}
    install -c -m 0755 ${B}/goto-fel ${D}${base_sbindir}
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

FILES_${PN} += "${base_libdir}/systemd/system/reflash-utility.service"
FILES_${PN} += "${base_sbindir}/goto-fel"
