SUMMARY = "Summit SOM WBx3 USB Boot initramfs Image"
DESCRIPTION = "Summit SOM WBx3 Manufacturing Provisioning USB Boot initramfs Image"

LICENSE = "Ezurio-Clause"

inherit image

export IMAGE_BASENAME = "${PN}"
IMAGE_NAME_SUFFIX ?= ""

REQUIRED_DISTRO_FEATURES += "summitsom-wbx3-initramfs"

PACKAGE_INSTALL = ""
IMAGE_FEATURES = ""
IMAGE_INSTALL = ""

ARCHIVE_NAME ?= "${IMAGE_BASENAME}-${MACHINE}-summit${IMAGE_VERSION_SUFFIX}"

addtask create_archive after do_image_complete before do_build

do_create_archive[depends] += "virtual/kernel:do_deploy virtual/bootloader:do_deploy"
do_create_archive() {
    cp -fL "${DEPLOY_DIR_IMAGE}/flash.bin" "${DEPLOY_DIR_IMAGE}/flash-initramfs.bin"
    cp -fL "${DEPLOY_DIR_IMAGE}/fitImage-image-${DISTRO}-${MACHINE}-${MACHINE}" "${DEPLOY_DIR_IMAGE}/fitImage-initramfs"

    if echo "${IMAGE_VERSION_SUFFIX}" | grep -xEq '\-[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+' ; then
        #shellcheck disable=SC2086
        tar --transform='s,.*/,,' -cvjhf "${DEPLOY_DIR_IMAGE}/${ARCHIVE_NAME}.tar.bz2" \
            ${DEPLOY_DIR_IMAGE}/flash-initramfs.bin \
            ${DEPLOY_DIR_IMAGE}/fitImage-initramfs
    fi
}
