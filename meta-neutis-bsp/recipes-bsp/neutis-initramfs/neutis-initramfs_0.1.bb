DESCRIPTION = "uInitrd for Emlid NeutisN5"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "core-image-minimal-initramfs"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${DEPLOY_DIR_IMAGE}/uInitrd ${WORKDIR}/${PN}-${PV}/uInitrd
    install -m 0644 ${WORKDIR}/${PN}-${PV}/uInitrd ${D}/boot/uInitrd
}

FILES_${PN} += "/boot/uInitrd"
