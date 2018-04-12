FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://usb0.network"

PACKAGECONFIG_append += "networkd resolved"

do_install_append() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/usb0.network ${D}${sysconfdir}/systemd/network
}

FILES_${PN} += "{sysconfdir}/systemd/network/usb0.network"
