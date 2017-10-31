FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "\
    file://udhcpd \
    file://udhcpd.conf \
    "

do_install_append() {
    install -d ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd ${D}${sysconfdir}/default
    install -c -m 0644 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}
}

# FIXME
# No idea why main-recipe removes busybox-udhcpd init.d script from /etc/init.d
# after do_install
do_packagedata_prepend() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/busybox-udhcpd ${D}${sysconfdir}/init.d/
}

FILES_${PN}-udhcpd = ""

FILES_${PN} += "\
    ${sysconfdir}/default/udhcpd \
    ${sysconfdir}/udhcpd.conf \
    ${sysconfdir}/init.d/busybox-udhcpd \
    "
