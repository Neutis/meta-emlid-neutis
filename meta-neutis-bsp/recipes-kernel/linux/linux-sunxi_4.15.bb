SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
COMPATIBLE_MACHINE = "(sun4i|sun5i|sun7i|sun8i|sun50i)"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-sunxi/:"

inherit kernel

require recipes-kernel/linux/linux-dtb.inc
require recipes-kernel/linux/linux.inc

# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base += "kernel-devicetree"

# Default is to use stable kernel version
# If you want to use latest git version set to "1"
DEFAULT_PREFERENCE = "-1" 

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"	
PV = "4.15+git${SRCPV}"
SRCREV_pn-${PN} = "df57458873da1a2a52e31c96cff43942c3557037"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.15.y \
        file://defconfig \
        file://0001-compilation-DT-add-overlay-compilation-support.patch \
        file://0002-arm64-dts-allwinner-add-overlays-for-a64-and-h5-SoCs.patch \
        file://0003-arm-dts-allwinner-add-overlays-for-a10-a20-and-h3.patch \
        file://0004-arm-dts-add-uart2-uart3-rts_cts-pins-for-sunxi-h3-h5.patch \
        file://0005-arm-dts-allwinner-add-r-uart-and-r-i2c-for-h3-h5-SoCs.patch \
        file://0006-fix-bt-ap6212-rtc-sun6i-Enable-LOSC-output-gating.patch \
        file://0007-net-rfkill-gpio-add-new-of_device_id-struct-compatib.patch \
        file://0008-drivers-spi-spidev-new-compatible-spidev.patch \
        file://0009-DT-emlid-neutis-n5-spi-overlays.patch \
        "

S = "${WORKDIR}/git"
