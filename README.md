meta-emid-neutis
==============
Emlid Neutis N5 board based on allwinner H5 SoC.

This layer depends on the additional layers:
- meta-sunxi
- meta-openembedded

Tested with neutis-image, core-image-base.

Copy `bblayers.conf.sample` and `bblayers.conf.sample` from `meta-neutis-distro/conf` to build directory.

Execute `bitbake neutis-image` to build image, storage device target - eMMc(/dev/mmcblk2).

You will find your image file here: `build/tmp/deploy/images/neutis/neutis-image-neutis-%timedate.rootfs.sunxi-sdimg`.

how to build - simple guide
==============
1. Choose directory for image and clone the yocto release.
```
mkdir yocto-image && cd yocto-image
git clone -b pyro git://git.yoctoproject.org/poky.git
cd poky
```
`IMAGE_PATH=.../yocto-image`

2. Clone all necessary layers.
```
git clone git@github.com:emlid/meta-sunxi.git -b emlid-dev
git clone git@github.com:openembedded/meta-openembedded.git -b pyro
git clone git@github.com:emlid/meta-emlid-neutis.git -b master
```

3. Update environment and copy the rigth conf files.
```
source ./oe-init-build-env ../build
cp $IMAGE_PATH/meta-emlid-neutis/meta-neutis-distro/conf/local.conf.sample conf/local.conf
```
Compare `conf/bblayers.conf` and `$IMAGE_PATH/meta-emlid-neutis/meta-neutis-distro/conf/bblayers.conf.sample`.
Add all layers to `conf/bblayers.conf` that are included in sample file.

4. Build the image.
```
bitbake neutis-image
```
