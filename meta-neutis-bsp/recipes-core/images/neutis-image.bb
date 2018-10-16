SUMMARY = "An image to run Emlid Neutis"
LICENSE = "MIT"

PACKAGE_CLASSES = "package_ipk"
IMAGE_FEATURES += "splash package-management ssh-server-openssh tools-debug"

IMAGE_INSTALL_append += "kernel-modules"
IMAGE_INSTALL_append += "bash"
IMAGE_INSTALL_append += "busybox"
IMAGE_INSTALL_append += "systemd"
IMAGE_INSTALL_append += "opkg"

IMAGE_INSTALL_append += "openssh"
IMAGE_INSTALL_append += "packagegroup-core-ssh-openssh"
IMAGE_INSTALL_append += "openssh-sftp-server"

IMAGE_INSTALL_append += "dhcpcd"
IMAGE_INSTALL_append += "dhcp-server dhcp-client"
IMAGE_INSTALL_append += "iw"

# WiFi/BT
IMAGE_INSTALL_append += "wireless-tools"
IMAGE_INSTALL_append += "wpa-supplicant"
IMAGE_INSTALL_append += "networkmanager"
IMAGE_INSTALL_append += "init-ifupdown"
IMAGE_INSTALL_append += "bluez5"
IMAGE_INSTALL_append += "ap6212-firmware"
IMAGE_INSTALL_append += "ap6212-bt-initialization"

# I2C
IMAGE_INSTALL_append += "i2c-tools"

# Utilities
IMAGE_INSTALL_append += "vim"
IMAGE_INSTALL_append += "git"
IMAGE_INSTALL_append += "tmux"
IMAGE_INSTALL_append += "screen"
IMAGE_INSTALL_append += "rsync"
IMAGE_INSTALL_append += "sudo"
IMAGE_INSTALL_append += "lsof"
IMAGE_INSTALL_append += "strace"
IMAGE_INSTALL_append += "htop"
IMAGE_INSTALL_append += "zip"

IMAGE_INSTALL_append += "make"
IMAGE_INSTALL_append += "cmake"

IMAGE_INSTALL_append += "parted"
IMAGE_INSTALL_append += "e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-tune2fs"
IMAGE_INSTALL_append += "e2fsprogs-badblocks libcomerr libss libe2p libext2fs dosfstools"

IMAGE_INSTALL_append += "rng-tools"

# Python
IMAGE_INSTALL_append += "python"
IMAGE_INSTALL_append += "python-dbus python-pygobject python-argparse"
IMAGE_INSTALL_append += "python-distutils python-pkgutil python-netserver"
IMAGE_INSTALL_append += "python-xmlrpc python-ctypes python-html python-json python-compile"
IMAGE_INSTALL_append += "python-misc python-numbers python-unittest python-pydoc"

# BSP
IMAGE_INSTALL_append += "reflash-utility"
IMAGE_INSTALL_append += "serial-number"
IMAGE_INSTALL_append += "openssl"
PREFERRED_VERSION_openssl = "1.0.2k"
IMAGE_INSTALL_append += "openssl-atecc508a openssl-atecc508a-dev"

inherit core-image allwinner-overlays

ROOTFS_PREPROCESS_COMMAND_append += "deploy_allwinner_device_tree_blobs; "

ROOTFS_POSTPROCESS_COMMAND += "clean_boot_dir ; "

clean_boot_dir () {
    rm -r ${IMAGE_ROOTFS}/boot/* || true
}
