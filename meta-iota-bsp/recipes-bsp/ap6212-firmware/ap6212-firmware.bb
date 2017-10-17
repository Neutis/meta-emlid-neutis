DESCRIPTION = "Firmware for Ampak AP6212"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.broadcom;md5=18b6b752a010900b9c111f528f8c8ffd"
FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI += "\
    file://LICENSE.broadcom \
    file://brcm_patchram_plus-armv8-repo \
    file://fw_bcm43438a0_apsta.bin \
    file://fw_bcm43438a0.bin \
    file://fw_bcm43438a0_p2p.bin \
    file://nvram.txt \
    file://fw_bcm43438a1_apsta.bin \
    file://fw_bcm43438a1.bin \
    file://fw_bcm43438a1_p2p.bin \
    file://nvram_ap6212.txt \
    file://bcm43438a0.hcd \
    file://bcm43438a1.hcd \
    file://brcmfmac43430-sdio.bin \
    file://brcmfmac43430-sdio.txt \
    file://brcmfmac43430a0-sdio.bin \
    file://brcmfmac43430a0-sdio.txt \
    "

do_install_append() {
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/brcm_patchram_plus-armv8-repo ${D}${bindir}/brcm_patchram_plus
    
    install -d ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a0_apsta.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a0.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a0_p2p.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/nvram.txt ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a1_apsta.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a1.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/fw_bcm43438a1_p2p.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/nvram_ap6212.txt ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/bcm43438a0.hcd ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/bcm43438a1.hcd ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/brcmfmac43430a0-sdio.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/brcmfmac43430-sdio.bin ${D}${base_libdir}/firmware/ap6212
    install -c -m 0644 ${WORKDIR}/brcmfmac43430-sdio.txt ${D}${base_libdir}/firmware/ap6212
    
    install -d ${D}${base_libdir}/firmware/brcm
    install -d ${D}${sysconfdir}/firmware/ap6212
    ln -sf ${base_libdir}/firmware/ap6212/brcmfmac43430a0-sdio.bin ${D}${base_libdir}/firmware/brcm/brcmfmac43430a0-sdio.bin
    ln -sf ${base_libdir}/firmware/ap6212/brcmfmac43430-sdio.txt ${D}${base_libdir}/firmware/brcm/brcmfmac43430a0-sdio.txt
    ln -sf ${base_libdir}/firmware/ap6212/brcmfmac43430-sdio.bin ${D}${base_libdir}/firmware/brcm/brcmfmac43430-sdio.bin
    ln -sf ${base_libdir}/firmware/ap6212/brcmfmac43430-sdio.txt ${D}${base_libdir}/firmware/brcm/brcmfmac43430-sdio.txt
    ln -sf ${base_libdir}/firmware/ap6212/bcm43438a0.hcd ${D}${sysconfdir}/firmware/ap6212/4343A0.hcd
    ln -sf ${base_libdir}/firmware/ap6212/bcm43438a1.hcd ${D}${sysconfdir}/firmware/ap6212/4343A1.hcd
}

inherit base

S = "${WORKDIR}"

FILES_${PN} += "\
    ${bindir}/brcm_patchram_plus \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a0_apsta.bin \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a0.bin \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a0_p2p.bin \
    ${base_libdir}/firmware/ap6212/nvram.txt \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a1_apsta.bin \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a1.bin \
    ${base_libdir}/firmware/ap6212/fw_bcm43438a1_p2p.bin \
    ${base_libdir}/firmware/ap6212/nvram_ap6212.txt \
    ${base_libdir}/firmware/ap6212/bcm43438a0.hcd \
    ${base_libdir}/firmware/ap6212/bcm43438a1.hcd \
    ${base_libdir}/firmware/ap6212/brcmfmac43430a0-sdio.bin \
    ${base_libdir}/firmware/ap6212/brcmfmac43430-sdio.txt \
    ${base_libdir}/firmware/ap6212/brcmfmac43430-sdio.bin \
    ${base_libdir}/firmware/brcm/brcmfmac43430a0-sdio.bin \
    ${base_libdir}/firmware/brcm/brcmfmac43430a0-sdio.txt \
    ${base_libdir}/firmware/brcm/brcmfmac43430-sdio.bin \
    ${base_libdir}/firmware/brcm/brcmfmac43430-sdio.txt \
    ${sysconfdir}/firmware/ap6212/4343A0.hcd \
    ${sysconfdir}/firmware/ap6212/4343A1.hcd \
    "
