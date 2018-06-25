FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://NetworkManager.conf"

do_install_append() {
    install -d ${D}${sysconfdir}/NetworkManager
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}${sysconfdir}/NetworkManager/NetworkManager.conf
}

FILES_${PN} += "${sysconfdir}/NetworkManager/NetworkManager.conf"
