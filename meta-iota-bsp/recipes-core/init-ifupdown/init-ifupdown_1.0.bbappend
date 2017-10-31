FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://hostap-interfaces"

do_install_append () {
    install -d ${D}${sysconfdir}/network
    install -c -m 0644 ${WORKDIR}/hostap-interfaces ${D}${sysconfdir}/network/hostap-interfaces
}

FILES_${PN} += "${sysconfdir}/network/hostap-interfaces"
