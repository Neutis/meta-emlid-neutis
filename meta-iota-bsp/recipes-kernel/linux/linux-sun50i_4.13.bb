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
SRCREV_pn-${PN} = "e9dde66df5f719b063aaea8acf073cbfc6952821"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;protocol=https;branch=linux-4.13.y \
        file://defconfig \
        file://001armbian-fixes.patch \
        file://002add-emlid-iota.patch \
        file://003add-spidevX-dts-overlay.patch \
        file://004add-usbhost1-dts-overlay.patch \
        file://005update-uart1-dts.patch \
        file://006add-r-uart-dts-and-r-i2c-overlay.patch \
        "

S = "${WORKDIR}/git"
