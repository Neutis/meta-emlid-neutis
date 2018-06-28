# TODO

DESCRIPTION = "uInitrd for Emlid NeutisN5"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://uInitrd"

S = "${WORKDIR}"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${S}/uInitrd ${D}/boot/uInitrd

    cp ${S}/uInitrd ${DEPLOY_DIR_IMAGE}/uInitrd
}

FILES_${PN} += "/boot/uInitrd"
