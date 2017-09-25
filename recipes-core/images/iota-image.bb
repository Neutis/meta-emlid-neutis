SUMMARY = "An image to run Emlid Iota"
LICENSE = "MIT"

# PACKAGE_CLASSES += "package_ipk"
IMAGE_FEATURES += "splash"
# IMAGE_FEATURES += "package-management ssh-server-openssh"

IMAGE_INSTALL_append += "kernel-modules"
IMAGE_INSTALL_append += "bash"
IMAGE_INSTALL_append += "systemd"
IMAGE_INSTALL_append += "opkg"

IMAGE_INSTALL_append += "openssh"
IMAGE_INSTALL_append += "packagegroup-core-ssh-openssh"
IMAGE_INSTALL_append += "openssh-sftp-server"

IMAGE_INSTALL_append += "dhcpcd"
IMAGE_INSTALL_append += "dhcp-server dhcp-client"
IMAGE_INSTALL_append += "iw"

# WiFi/BT
IMAGE_INSTALL_append += "hostapd"
IMAGE_INSTALL_append += "wireless-tools"
IMAGE_INSTALL_append += "wpa-supplicant"
IMAGE_INSTALL_append += "networkmanager"
IMAGE_INSTALL_append += "bluez5"
# IMAGE_INSTALL_append += "packagegroup-tools-bluetooth"
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
IMAGE_INSTALL_append += "parted"
IMAGE_INSTALL_append += "strace"
IMAGE_INSTALL_append += "htop"

# Python
# IMAGE_INSTALL_append += "python"

inherit core-image
