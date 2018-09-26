DESCRIPTION="Upstream's U-boot fw-utils configured for Neutis"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot/:"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit pythonnative

DEPENDS += "dtc-native swig-native python-native python-dev"

SRC_URI = "git://git.denx.de/u-boot.git;protocol=https \
           file://0001-v2018_01-arch-arm-new-board-Emlid-Neutis-N5-support.patch \
           "

SRCREV = "f3dd87e0b98999a78e500e8c6d2b063ebadf535a"

PV = "v2018.01+git${SRCPV}"

do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake envtools
}
