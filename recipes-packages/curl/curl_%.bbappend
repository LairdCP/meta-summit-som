PACKAGECONFIG_class-target = "${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)} ssl libidn threaded-resolver verbose zlib tftp libssh"

PACKAGECONFIG[libssh] = "--with-libssh,--without-libssh,libssh"
