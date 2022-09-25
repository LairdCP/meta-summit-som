[ -z "${1}" ] && { echo "Usage: $0 <version>"; exit 1; }

file="radio-stack-som8mp-hashes.inc"
prefix="https://files.devops.rfpros.com/builds/linux"
ver=${1}

calc_file () {
  name=/tmp/${1##*/}
  wget -P /tmp/ ${1} || exit 1
  echo "SRC_URI[${2}.md5sum] = \"$(md5sum ${name} | awk '{print $1}')\"" >> ${file}
  echo "SRC_URI[${2}.sha256sum] = \"$(sha256sum ${name} | awk '{print $1}')\"" >> ${file}
  rm -f ${name}
}

echo -e "PV = \"${ver}\"\n" > ${file}

for i in aarch64
do
  calc_file "${prefix}/summit_supplicant/laird/${ver}/summit_supplicant-${i}-${ver}.tar.bz2" "summit-supplicant-${i}"
  calc_file "${prefix}/adaptive_ww/laird/${ver}/adaptive_ww-${i}-${ver}.tar.bz2" "adaptive-ww-${i}"
done

calc_file "${prefix}/adaptive_bt/src/${ver}/adaptive_bt-src-${ver}.tar.gz" "adaptive-bt"
calc_file "${prefix}/lrd-network-manager/src/${ver}/lrd-network-manager-src-${ver}.tar.xz" "lrd-network-manager"
calc_file "${prefix}/backports/laird/${ver}/backports-laird-${ver}.tar.bz2" "backports-laird"
calc_file "${prefix}/firmware/${ver}/laird-som8mp-radio-firmware-${ver}.tar.bz2" "som8mp-radio-firmware"

echo "PVN = \"${ver}\"" > radio-stack-som8mp-version-git.inc
