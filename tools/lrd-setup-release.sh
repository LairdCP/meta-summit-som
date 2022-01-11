#!/bin/sh
#
# i.MX Yocto Project Build Environment Setup Script
#
# Copyright (C) 2011-2016 Freescale Semiconductor
# Copyright 2017 NXP
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


. sources/meta-imx/tools/setup-utils.sh

CWD=`pwd`
PROGNAME="setup-environment"

exit_message ()
{
   echo "To return to this build environment later please run:"
   echo "    source setup-environment <build_dir>"
}

usage()
{
    echo
    echo "Usage: source $0
    Optional parameters: [-b build-dir] [-i] [-r] [-h]"
echo "
    * [-b build-dir]: Build directory, if unspecified script uses 'build' as output directory
    * [-i]: Laird Connectivity internal build (equivalent of MSD_BINARIES_SOURCE_LOCATION)
    * [-r]: Add meta-laird-cp to install if standalone radio development desired
    * [-h]: help
"
}

clean_up()
{
    unset CWD BUILD_DIR FSLDISTRO
    unset fsl_setup_help fsl_setup_error fsl_setup_flag
    unset fsl_setup_lrd fsl_setup_radio
    unset usage clean_up
    unset ARM_DIR META_FSL_BSP_RELEASE
    exit_message clean_up
}

# get command line options
OLD_OPTIND=$OPTIND
unset FSLDISTRO

while getopts ":birh" fsl_setup_flag
do
    case $fsl_setup_flag in
        b) BUILD_DIR="$OPTARG";
           echo
           echo " Build directory is " $BUILD_DIR
           ;;
        i) fsl_setup_lrd=true
           ;;
        r) fsl_setup_radio=true
           ;;
        h) fsl_setup_help=true
           ;;
        *) fsl_setup_error=true
           ;;
    esac
done
shift $((OPTIND-1))
if [ $# -ne 0 ]; then
    fsl_setup_error=true
    echo "Invalid command line ending: '$@'"
fi

OPTIND=$OLD_OPTIND
if test $fsl_setup_help; then
    usage && clean_up && return 1
elif test $fsl_setup_error; then
    clean_up && return 1
fi

if [ -z "$DISTRO" ]; then
    if [ -z "$FSLDISTRO" ]; then
        FSLDISTRO='summitsom-cmd'
    fi
else
    FSLDISTRO="$DISTRO"
fi

if [ -z "$BUILD_DIR" ]; then
    BUILD_DIR='build'
fi

if [ -z "$MACHINE" ]; then
    echo setting to default machine
    MACHINE='imx8mp-summitsom_2g'
fi

# Cleanup previous meta-freescale/EULA overrides
cd $CWD/sources/meta-freescale
if [ -h EULA ]; then
    echo Cleanup meta-freescale/EULA...
    git checkout -- EULA
fi
if [ ! -f classes/fsl-eula-unpack.bbclass ]; then
    echo Cleanup meta-freescale/classes/fsl-eula-unpack.bbclass...
    git checkout -- classes/fsl-eula-unpack.bbclass
fi
cd $CWD

# Override the click-through in meta-freescale/EULA
FSL_EULA_FILE=$CWD/sources/meta-imx/EULA.txt

[ -z "$SDKMACHINE" ] && SDKMACHINE=$(uname -m)

# Set up the basic yocto environment
if [ -z "$DISTRO" ]; then
   DISTRO=$FSLDISTRO MACHINE=$MACHINE SDKMACHINE=$SDKMACHINE PACKAGE_CLASSES=package_ipk . ./$PROGNAME $BUILD_DIR
else
   MACHINE=$MACHINE SDKMACHINE=$SDKMACHINE PACKAGE_CLASSES=package_ipk . ./$PROGNAME $BUILD_DIR
fi

# Point to the current directory since the last command changed the directory to $BUILD_DIR
BUILD_DIR=.

if [ ! -e $BUILD_DIR/conf/local.conf ]; then
    echo
    echo " ERROR - No build directory is set yet. Run the 'setup-environment' script before running this script to create " $BUILD_DIR
    echo
    return 1
fi

# On the first script run, backup the local.conf file
# Consecutive runs, it restores the backup and changes are appended on this one.
if [ ! -e $BUILD_DIR/conf/local.conf.org ]; then
    cp $BUILD_DIR/conf/local.conf $BUILD_DIR/conf/local.conf.org
else
    cp $BUILD_DIR/conf/local.conf.org $BUILD_DIR/conf/local.conf
fi

echo >> conf/local.conf

if test $fsl_setup_lrd; then
    echo 'OVERRIDES .= ":laird-internal"' >> conf/local.conf
    echo >> conf/local.conf
fi

test $fsl_setup_radio && \
echo 'BBMASK += " \
    meta-laird-cp/recipes-packages/openssl \
    meta-laird-cp/recipes-packages/.*/.*openssl10.* \
"
' >> conf/local.conf

if [ ! -e $BUILD_DIR/conf/bblayers.conf.org ]; then
    cp $BUILD_DIR/conf/bblayers.conf $BUILD_DIR/conf/bblayers.conf.org
else
    cp $BUILD_DIR/conf/bblayers.conf.org $BUILD_DIR/conf/bblayers.conf
fi

echo "" >> $BUILD_DIR/conf/bblayers.conf
echo "# i.MX Laird Connectivity Yocto Release layers" >> $BUILD_DIR/conf/bblayers.conf
hook_in_layer meta-imx/meta-bsp
hook_in_layer meta-imx/meta-sdk
hook_in_layer meta-imx/meta-ml

hook_in_layer meta-openembedded/meta-networking
hook_in_layer meta-openembedded/meta-filesystems

hook_in_layer meta-qt5
hook_in_layer meta-python2

hook_in_layer meta-swupdate
hook_in_layer meta-timesys

hook_in_layer meta-lrd-summitsom
hook_in_layer meta-lrd-radio-mfg

test $fsl_setup_radio && hook_in_layer meta-laird-cp

echo BSPDIR=$BSPDIR
echo BUILD_DIR=$BUILD_DIR

cd  $BUILD_DIR
clean_up
unset FSLDISTRO
