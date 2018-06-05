DESCRIPTION = "Bluetooth rfkill event daemon for Bluetooth chips"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS_${PN} = "systemd bash"
DEPENDS = "systemd bash bluez5"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

inherit systemd

SRC_URI += "\
        file://bluetooth_rfkill_event.c \
        file://bluetooth-rfkill-event.service \
"

SYSTEMD_SERVICE_${PN} = "bluetooth-rfkill-event.service"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_RESTART = "false"

TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}"

do_compile() {
        ${CC} $CFLAGS -o bluetooth_rfkill_event bluetooth_rfkill_event.c ${INC_DIRS} ${LIBS}
}

do_install() {
        install -v -d ${D}${bindir}
        install -m 0755 bluetooth_rfkill_event ${D}${bindir}
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/bluetooth-rfkill-event.service ${D}${systemd_unitdir}/system
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

FILES_${PN} += "${base_libdir}/systemd/system/bluetooth-rfkill-event.service"
FILES_${PN} += "${bindir}/bluetooth_rfkill_event"
