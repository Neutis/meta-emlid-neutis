SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
COMPATIBLE_MACHINE = "(sun4i|sun5i|sun7i|sun8i|sun50i)"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-sunxi/:"

LIC_FILES_CHKSUM_remove += "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
LIC_FILES_CHKSUM_append += "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

require recipes-kernel/linux/linux.inc

# Pull in the devicetree files into the rootfs
RDEPENDS_${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

# Explicitly depend on bison-native for deterministic builds, as it is required
# for the build
do_kernel_configme[depends] += "bison-native:do_populate_sysroot"

# Default is to use stable kernel version
# If you want to use latest git version set to "1"
DEFAULT_PREFERENCE = "-1"

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"
PV = "4.17+git${SRCPV}"
SRCREV_pn-${PN} = "3816828a8cd06c5e0a0a9e0456013d61d979d975"
KBRANCH = "linux-4.17.y"

SRC_URI += "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.17.y \
        file://defconfig \
        file://0001-megous-linux-ths-4.17.patch \
        file://0002-compilation-DT-add-overlay-compilation-support.patch \
        file://0003-arm64-dts-allwinner-add-overlays-for-a64-and-h5-SoCs.patch \
        file://0004-arm-dts-allwinner-add-overlays-for-a10-a20-and-h3-So.patch \
        file://0005-arm-dts-add-uart2-uart3-rts_cts-pins-for-sunxi-h3-h5.patch \
        file://0006-arm-dts-allwinner-add-r-uart-and-r-i2c-for-sunxi-h3-.patch \
        file://0007-rtc-rtc-sun6i-Enable-LOSC-output-gating.patch \
        file://0008-net-rfkill-gpio-add-new-of_device_id-struct-compatib.patch \
        file://0009-drivers-spi-spidev-new-compatible-spidev.patch \
        file://0010-dts-allwinner-add-new-board-Emlid-Neutis-N5.patch \
        file://0011-arm-dts-allwinner-add-DT-layers-to-enable-spi-0-1.patch \
        file://0012-arm64-dts-allwinner-add-sun50i-h5-ethernet-100-overl.patch \
        file://0013-arm-dts-allwinner-add-hdmi-overlay.patch \
        file://0014-dts-allwinner-make-conform-MMC0-pins-declaration-con.patch \
        file://0015-dts-allwinner-audio-codec-add-LINEIN-and-MIC2-routin.patch \
        file://0016-arm64-dts-allwinner-add-operating-points-cpu_opp_tab.patch \
        file://0017-allwinner-h3-h5-camera-support.patch \
        file://0018-arm64-dts-allwinner-add-camera.patch \
        file://0019-arm64-allwinner-add-dtsi-file-for-emlid-neutis-n5.patch \
        file://0020-arm64-allwinner-add-dts-for-neutis-n5-devboard.patch \
        file://0021-neutis-add-cooling-maps-and-update-opp.patch \
        file://0022-bluetooth-serdev-enable.patch \
        "

S = "${WORKDIR}/git"
