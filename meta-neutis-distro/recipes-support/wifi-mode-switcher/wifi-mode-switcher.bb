DESCRIPTION = "WiFi host/client switcher"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "\
    file://wifi-mode-switcher \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

RDEPENDS_${PN} += "bash"

inherit base

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -c -m 0755 ${B}/wifi-mode-switcher ${D}${bindir}
}

FILES_${PN} += "${bindir}/wifi-mode-switcher"
