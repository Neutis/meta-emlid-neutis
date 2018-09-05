DESCRIPTION="Upstream's U-boot fw-utils configured for Neutis"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot/:"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

inherit pythonnative

DEPENDS += "dtc-native swig-native python-native"

SRC_URI = "git://git.denx.de/u-boot.git;protocol=https \
           file://0001-v2018_07-arch-arm-new-board-Emlid-Neutis-N5-support.patch \
           "
SRCREV = "8c5d4fd0ec222701598a27b26ab7265d4cee45a3"

PV = "v2018.07+git${SRCPV}"

do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake envtools
}
