FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://init-live.sh \
            file://resize.sh \
            file://setup.sh \
            file://fsck.sh \
            file://util.sh \
           "

do_install_append () {
    install -d ${D}/scripts
    install -m 0644 resize.sh ${D}/scripts/resize.sh
    install -m 0644 setup.sh ${D}/scripts/setup.sh
    install -m 0644 fsck.sh ${D}/scripts/fsck.sh
    install -m 0644 util.sh ${D}/scripts/util.sh
}

FILES_${PN} += "/scripts/resize.sh"
FILES_${PN} += "/scripts/setup.sh"
FILES_${PN} += "/scripts/fsck.sh"
FILES_${PN} += "/scripts/util.sh"
