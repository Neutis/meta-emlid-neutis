IMAGE_INSTALL_append += "coreutils"
IMAGE_INSTALL_append += "util-linux"
IMAGE_INSTALL_append += "u-boot-fw-utils"

# Mender
require recipes-core/images/neutis-mender-image.inc

