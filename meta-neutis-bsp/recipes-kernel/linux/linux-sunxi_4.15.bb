SECTION = "kernel"
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
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
PV = "4.15+git${SRCPV}"
SRCREV_pn-${PN} = "df57458873da1a2a52e31c96cff43942c3557037"
KBRANCH = "linux-4.15.y"

SRC_URI += "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.15.y \
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
        file://megous-ths-4.15/0001-arm64-config-Fix-PINCTRL-not-being-available-in-Kcon.patch \
        file://megous-ths-4.15/0002-clk-sunxi-ng-Set-maximum-M-1-for-H3-pll-cpux-clock.patch \
        file://megous-ths-4.15/0003-clk-sunxi-ng-Allow-to-limit-the-use-of-NKMP-clock-s-.patch \
        file://megous-ths-4.15/0004-clk-sunxi-ng-Limit-pll_cpux-P-factor-for-rates-288MH.patch \
        file://megous-ths-4.15/0005-thermal-sun8i_ths-Add-support-for-the-thermal-sensor.patch \
        file://megous-ths-4.15/0006-dt-bindings-document-sun8i_ths-H3-thermal-sensor-dri.patch \
        file://megous-ths-4.15/0007-cpufreq-dt-platdev-Add-allwinner-sun50i-h5-compatibl.patch \
        file://megous-ths-4.15/0008-thermal-sun8i-ths-Allow-to-enable-thermal-sensor-dri.patch \
        file://megous-ths-4.15/0009-ARM-dts-sun8i-Add-cpu0-label-to-sun8i-h3.dtsi.patch \
        file://megous-ths-4.15/0010-ARM-dts-sun8i-h3-Add-clock-frequency.patch \
        file://megous-ths-4.15/0011-arm64-dts-sun50i-h5-Add-cpu0-label.patch \
        file://megous-ths-4.15/0012-ARM-dts-sun8i-Add-thermal-sensor-node-to-H3-dts.patch \
        file://megous-ths-4.15/0013-arm64-dts-sun50i-h5-Configure-cpufreq.patch \
        file://0010-arm64-dts-allwinner-add-operating-points-cpu_opp_tab-Emlid-Neutis-N5.patch \
        file://0011-arm64-dts-allwinner-add-sun50i-h5-ethernet-100-overlay.patch \
        "


S = "${WORKDIR}/git"
