FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "\
    file://udhcpd \
    file://udhcpd.conf \
    "

RRECOMMENDS_${PN} += "${PN}-udhcpd"

do_install_append() {
    install -d ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}
}

FILES_${PN}-udhcpd += "\
    ${sysconfdir}/default/udhcpd \
    ${sysconfdir}/udhcpd.conf \
    "
