FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add patch for module bcm43xx
# Add patches for QCA modules with Qca6174 and Qca9377-3 chips
SRC_URI_remove = "file://0007-MLK-23858-profiles-audio-increased-the-MTU-size-to-M.patch"

SRC_URI += " \
        file://0007-MLK-23858-profiles-audio-increased-the-MTU-size-to-M.patch \
"
