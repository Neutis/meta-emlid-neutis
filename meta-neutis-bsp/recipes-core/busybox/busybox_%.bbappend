FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "\
    file://udhcpd \
    file://udhcpd-wlan0.conf \
    file://udhcpd-usb0.conf \
    file://udhcpd-wlan0.service \
    file://udhcpd-usb0.service \
    "

RRECOMMENDS_${PN} += "${PN}-udhcpd"
SYSTEMD_SERVICE_${PN}-udhcpd = "udhcpd-wlan0.service udhcpd-usb0.service"

do_install_append() {
    install -d ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd-wlan0.conf ${D}${sysconfdir}
    install -c -m 0644 ${WORKDIR}/udhcpd-usb0.conf ${D}${sysconfdir}

    install -d ${D}${systemd_unitdir}/system
    install -c -m 0644 ${WORKDIR}/udhcpd-wlan0.service ${D}${systemd_unitdir}/system
    install -c -m 0644 ${WORKDIR}/udhcpd-usb0.service ${D}${systemd_unitdir}/system

    # Enable services
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf ${systemd_unitdir}/system/udhcpd-usb0.service \
        ${D}${sysconfdir}/systemd/system/multi-user.target.wants/udhcpd-usb0.service
}

FILES_${PN}-udhcpd += "\
    ${sysconfdir}/default/udhcpd \
    ${sysconfdir}/udhcpd-wpan0.conf \
    ${sysconfdir}/udhcpd-usb0.conf \
    ${systemd_unitdir}/system/udhcpd-wlan0.service \
    ${systemd_unitdir}/system/udhcpd-usb0.service \
    "
