DESCRIPTION = "Initramfs for Emlid Neutis"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "file://neutis-initramfs"

S = "${WORKDIR}"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${S}/neutis-initramfs ${D}/boot/initramfs.cpio.gz

    cp ${S}/neutis-initramfs ${DEPLOY_DIR_IMAGE}/initramfs.cpio.gz
}

FILES_${PN} += "/boot/initramfs.cpio.gz"
