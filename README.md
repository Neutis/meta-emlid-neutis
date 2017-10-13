meta-iota
==============
Emlid Iota board based on allwinner H5 SoC.

This layer depends on the additional layer:
- meta-sunxi from `https://github.com/AD-Aleksandrov`
- meta-openembedded

Tested with iota-image, core-image-base.

Copy `bblayers.conf.sample` and `bblayers.conf.sample` from `meta-iota-distro/conf` to build directory.

Execute `bitbake iota-image` to build image.

You will find your image file here: `build/tmp/deploy/images/iota/iota-image-iota-%timedate.rootfs.sunxi-sdimg`.
