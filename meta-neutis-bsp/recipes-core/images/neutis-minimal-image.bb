SUMMARY = "An image to run Emlid Neutis"
LICENSE = "MIT"

PACKAGE_CLASSES = "package_ipk"
IMAGE_FEATURES += "splash"

IMAGE_INSTALL_append += "kernel-modules"
IMAGE_INSTALL_append += "bash"
IMAGE_INSTALL_append += "busybox"

# WiFi
IMAGE_INSTALL_append += "ap6212-firmware"

# I2C
IMAGE_INSTALL_append += "i2c-tools"

inherit core-image

ROOTFS_POSTPROCESS_COMMAND += "clean_boot_dir ; "

clean_boot_dir () {
    rm -r ${IMAGE_ROOTFS}/boot/*
}
