DESCRIPTION="Upstream's U-boot fw-utils configured for Neutis"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot/:"

inherit pythonnative

DEPENDS += "dtc-native swig-native python-native"

SRC_URI = "git://git.denx.de/u-boot.git;protocol=https \
           file://0001-arm-sunxi-new-board-Emlid-Neutis-N5.patch \
           "
SRCREV = "f3dd87e0b98999a78e500e8c6d2b063ebadf535a"

PV = "v2018.01+git${SRCPV}"

do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake envtools
}
