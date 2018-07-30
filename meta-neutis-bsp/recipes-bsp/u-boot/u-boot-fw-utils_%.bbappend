DESCRIPTION="Upstream's U-boot fw-utils configured for Neutis"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-sun50i/:"

inherit pythonnative
DEPENDS += "dtc-native swig-native python-dev python-native"

SRC_URI = "git://git@github.com/emlid/u-boot-neutis.git;protocol=ssh"

SRCREV = "bbbabc1683f41a7757620b06609f6bb9c927078a"

PV = "v2018.01+git${SRCPV}"

do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake envtools
}
