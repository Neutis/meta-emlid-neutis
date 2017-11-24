SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
COMPATIBLE_MACHINE = "(sun4i|sun5i|sun7i|sun8i|sun50i)"
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
PV = "4.14+git${SRCPV}"
SRCREV_pn-${PN} = "bebc6082da0a9f5d47a1ea2edc099bf671058bd4"

SRC_URI = "git://git@github.com/emlid/linux-iota.git;protocol=ssh;branch=4.14-emlid-iota \
        file://defconfig \
        file://0001-arm64-dts-allwinner-add-DT-file-for-emlid-iota-board.patch \
        file://0002-compilation-DT-add-overlay-compilation-support.patch \
        file://0003-arm64-dts-allwinner-add-overlays-for-a64-and-h5-SoCs.patch \
        file://0004-arm-dts-allwinner-add-overlays-for-a10-a20-and-h3.patch \
        file://0005-arm-dts-add-uart2-uart3-rts_cts-pins-for-sunxi-h3-h5.patch \
        file://0006-arm-dts-allwinner-enable-spidev-for-emlid-iota-add-new-overlays.patch \
        "

S = "${WORKDIR}/git"
