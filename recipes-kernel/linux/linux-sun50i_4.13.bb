SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
COMPATIBLE_MACHINE = "(sun50i)"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-sun50i/:"

inherit kernel

require recipes-kernel/linux/linux-dtb.inc
require recipes-kernel/linux/linux.inc

# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base += "kernel-devicetree"

# Default is to use stable kernel version
# If you want to use latest git version set to "1"
DEFAULT_PREFERENCE = "-1" 

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"
	
PV = "4.13+git${SRCPV}"
SRCREV_pn-${PN} = "569dbb88e80deb68974ef6fdd6a13edb9d686261"

SRC_URI = "git://github.com/torvalds/linux.git;protocol=https;branch=master \
        file://defconfig \
        file://armbian-fixes.patch \
        file://add-emlid-iota.patch \
        "
S = "${WORKDIR}/git"
