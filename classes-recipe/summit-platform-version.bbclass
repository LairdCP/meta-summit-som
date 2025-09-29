
include ${@'summit-platform-version.inc' if not d.getVar('SUMMIT_PLATFORM_VERSION') else ''}

python () {
    platform_version = d.getVar('SUMMIT_PLATFORM_VERSION')
    if platform_version.startswith('LRD-REL-'):
        d.setVar('PV', platform_version.split('-')[2])
        d.setVar('SUMMIT_PLATFORM_BRANCH', 'nobranch=1')
    else:
        d.setVar('PV', platform_version + '+git')
        d.setVar('SUMMIT_PLATFORM_BRANCH', 'branch=' + platform_version)
}

SRCREV = "${@ d.getVar('SUMMIT_PLATFORM_VERSION') if d.getVar('SUMMIT_PLATFORM_VERSION').startswith('LRD-REL-') else d.getVar('AUTOREV')}"

SUMMIT_EXTERNAL_GIT_URI ?= "git://github.com/Ezurio"
SUMMIT_EXTERNAL_GIT_PROTOCOL ?= "https"
SUMMIT_EXTERNAL_GIT_SUFFIX ?= "protocol=${SUMMIT_EXTERNAL_GIT_PROTOCOL};${SUMMIT_PLATFORM_BRANCH}"

SUMMIT_INTERNAL_GIT_URI ?= "git://git@github.com/rfpros"
SUMMIT_INTERNAL_GIT_PROTOCOL ?= "ssh"
SUMMIT_INTERNAL_GIT_SUFFIX ?= "protocol=${SUMMIT_INTERNAL_GIT_PROTOCOL};${SUMMIT_PLATFORM_BRANCH}"
