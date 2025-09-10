PACKAGECONFIG:remove:summit-b2qt = " \
    ${@bb.utils.contains('LICENSE_FLAGS_ACCEPTED', 'commercial', '', 'faad', d)} \
    "
