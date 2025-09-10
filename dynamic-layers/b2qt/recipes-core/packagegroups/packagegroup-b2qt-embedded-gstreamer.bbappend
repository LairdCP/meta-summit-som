RDEPENDS:${PN}:remove:summit-b2qt = " \
    ${@bb.utils.contains('LICENSE_FLAGS_ACCEPTED', 'commercial', '', 'gstreamer1.0-plugins-ugly-meta', d)} \
    ${@bb.utils.contains('LICENSE_FLAGS_ACCEPTED', 'commercial', '', 'gstreamer1.0-libav', d)} \
    "
