ROOTFS_POSTPROCESS_COMMAND += "bootstrap_boot_directory ; "

bootstrap_boot_directory () {
    cp -r ${DEPLOY_DIR_IMAGE}/allwinner ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/Image ${IMAGE_ROOTFS}/boot/Image
    cp ${DEPLOY_DIR_IMAGE}/Env.txt ${IMAGE_ROOTFS}/boot/
    cp ${DEPLOY_DIR_IMAGE}/uInitrd ${IMAGE_ROOTFS}/boot/uInitrd
}

