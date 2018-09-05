DESCRIPTION="Upstream's U-boot configured for sunxi devices"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot/:"

require recipes-bsp/u-boot/u-boot.inc
inherit pythonnative

PROVIDES += "u-boot"
RPROVIDES_${PN} += "u-boot"

DEPENDS += "dtc-native swig-native python-native"
DEPENDS_append_sun50i += "atf-sunxi"

LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "\
file://Licenses/Exceptions;md5=338a7cb1e52d0d1951f83e15319a3fe7 \
file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
file://Licenses/eCos-2.0.txt;md5=b338cb12196b5175acd3aa63b0a0805c \
file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
file://Licenses/ibm-pibs.txt;md5=c49502a55e35e0a8a1dc271d944d6dba \
file://Licenses/isc.txt;md5=ec65f921308235311f34b79d844587eb \
file://Licenses/lgpl-2.0.txt;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
file://Licenses/x11.txt;md5=b46f176c847b8742db02126fb8af92e2 \
"

COMPATIBLE_MACHINE = "(sun50i)"

DEFAULT_PREFERENCE_sun50i="1"
SRC_URI = "git://git.denx.de/u-boot.git;protocol=https \
           file://0001-v2018_07-arch-arm-new-board-Emlid-Neutis-N5-support.patch \
           file://boot.cmd \
           file://Env.txt \
           "

SRCREV = "8c5d4fd0ec222701598a27b26ab7265d4cee45a3"

PV = "v2018.07+git${SRCPV}"
PE = "2"

S = "${WORKDIR}/git"

UBOOT_BINARY = "u-boot-sunxi-with-spl.bin"

UBOOT_ENV_SUFFIX = "scr"
UBOOT_ENV = "boot"

do_configure_append() {
    cp ${WORKDIR}/Env.txt ${DEPLOY_DIR_IMAGE}
}

EXTRA_OEMAKE_append_sun50i = " BL31=${DEPLOY_DIR_IMAGE}/bl31.bin "

do_compile_sun50i[depends] += "atf-sunxi:do_deploy"

do_compile_append() {
    ${B}/tools/mkimage -C none -A arm -T script -d ${WORKDIR}/boot.cmd ${WORKDIR}/${UBOOT_ENV_BINARY}
}
