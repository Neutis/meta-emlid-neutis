SUMMARY = "OpenSSL Engine implementation using ATECC508 for ECC key storage, \
           ECDSA sign/verify, ECDH, and FIPS Random Number Generator (RNG)."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3fdaa96f37898a0641820700bbf5f7b8"

FILESEXTRAPATHS_prepend := "${THISDIR}/files/:"

SRC_URI = "git://git@github.com/MicrochipTech/cryptoauth-openssl-engine.git;protocol=http;branch=master"
SRC_URI += "file://0001-Set-I2C-linux-interface-for-the-communication-enable-shared-lib-build.patch \
           "
SRCREV = "0cc4a3a562f070e508fe21d476394e59e99e567b"

DEPENDS = "openssl"
RDEPENDS_${PN} = "openssl"

INSANE_SKIP_${PN} = "ldflags"

S = "${WORKDIR}/git"
LIB_SRC_PATH = "${S}/cryptoauthlib"
LIB_NAME = "ateccssl"
SHARED_LIB = "libateccssl.so"
VER = "1"

do_compile() {
    cd ${LIB_SRC_PATH}
    oe_runmake libateccssl
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755 "${LIB_SRC_PATH}/.build/${SHARED_LIB}" ${D}${libdir}/${SHARED_LIB}.${VER}
    ln -sf ${libdir}/${SHARED_LIB}.${VER} ${D}${libdir}/${SHARED_LIB}

    cd "${LIB_SRC_PATH}/lib"

    for header in `find * -name "*.h"`
    do
        install -d ${D}${includedir}/${LIB_NAME}/$(dirname ${header})
        install -m 0644 ${LIB_SRC_PATH}/lib/${header} ${D}${includedir}/${LIB_NAME}/${header}
    done
}

FILES_${PN} += "${libdir}/${SHARED_LIB}* \
                ${includedir}/${LIB_NAME}/*"
FILES_${PN}-dbg += "${includedir}/.debug/${SHARED_LIB}*"
