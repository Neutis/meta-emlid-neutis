DESCRIPTION = "Read Emlid NeutisN5's serial number"
SECTION = "base"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://serial-number.c"
SRC_URI += "file://serial_number.service"

DEPENDS += "openssl-atecc508a systemd bash"
RDEPENDS_${PN} += "openssl-atecc508a systemd bash"

inherit systemd
SYSTEMD_SERVICE_${PN} = "serial_number.service"
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_RESTART = "false"

S = "${WORKDIR}"

INSANE_SKIP_${PN} = "file-rdeps"

INC_DIRS = "-I${STAGING_INCDIR}/ateccssl"
LIB_DIRS = "-L${STAGING_LIBDIR}"
LIBS = "-lateccssl"
SERIAL_MAGIC ?= "7853"

do_compile() {
	${CC} -DSERIAL_APPEND=\"${SERIAL_MAGIC}\" ${CFLAGS} ${LDFLAGS} ${LIB_DIRS} ${INC_DIRS} ${S}/serial-number.c -o serial_number ${LIBS}
}

do_install() {
	install -d ${D}${bindir}
	install -c -m 0755 ${B}/serial_number ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -c -m 0644 ${S}/serial_number.service ${D}${systemd_unitdir}/system
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

FILES_${PN} += "${bindir}/serial_number"
FILES_${PN} += "${base_libdir}/systemd/system/serial_number.service"
