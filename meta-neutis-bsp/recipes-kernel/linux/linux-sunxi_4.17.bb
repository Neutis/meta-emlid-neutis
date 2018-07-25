SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
COMPATIBLE_MACHINE = "(sun4i|sun5i|sun7i|sun8i|sun50i)"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-sunxi/:"

inherit kernel

require recipes-kernel/linux/linux.inc

# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base += "kernel-devicetree"

# Default is to use stable kernel version
# If you want to use latest git version set to "1"
DEFAULT_PREFERENCE = "-1"

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"
PV = "4.17+git${SRCPV}"
SRCREV_pn-${PN} = "3816828a8cd06c5e0a0a9e0456013d61d979d975"
KBRANCH = "linux-4.17.y"

SRC_URI += "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.17.y \
        file://defconfig \
        "


S = "${WORKDIR}/git"
