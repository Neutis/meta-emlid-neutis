DEPLOYDIR = "${WORKDIR}/deploy-${PN}-image-complete"
COMPATIBLE_HOST = "(i.86|x86_64|aarch64).*-linux"
IMAGE_BASENAME = "neutis-initramfs"

DEPENDS = "u-boot-mkimage-native"

PACKAGE_INSTALL_remove = "initramfs-live-install-efi"
PACKAGE_INSTALL_remove = "${INITRAMFS_SCRIPTS}"
PACKAGE_INSTALL += "e2fsprogs"
PACKAGE_INSTALL += "parted"
PACKAGE_INSTALL += "dosfstools"
PACKAGE_INSTALL += "e2fsprogs-resize2fs"
PACKAGE_INSTALL += "initramfs-live-boot"

do_make_ramdisk() {
    uboot-mkimage -A arm64 -T ramdisk -C none -n uInitrd -d ${DEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.cpio.gz ${DEPLOY_DIR_IMAGE}/uInitrd
}
addtask make_ramdisk after do_image_cpio before do_image_complete
