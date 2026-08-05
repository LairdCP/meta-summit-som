# Avoid eudev's libudev sub-package colliding with systemd's "libudev1" package
# (they auto-derive the same Debian-style name from the shared libudev.so.1
# soname). This guarantees the two can never produce identically-named
# shared-area/SBOM artifacts, independent of DISTRO or build/sstate history.
DEBIANNAME:libudev = "libudev1-eudev"
