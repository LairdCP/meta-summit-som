---
layout: page
title: "Developing a custom image with Yocto"
category: Summit SOM, Carbon
order: 3
product: Summit SOM, Carbon
technology: soms
---

The SOM (and DVK) and Carbon support the creation of custom images via the [Yocto](https://www.yoctoproject.org) Project, and the steps below document how to setup a Yocto build environment for the SOM (and DVK) and build an image and also outline some next steps for integrating the SOM into a custom board image.

## Prerequisites

### Yocto Project Prerequisites
If completely new to the Yocto Project, the [documentation](https://docs.yoctoproject.org) site provides a [Quick Build](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html) getting started guide focused on the Yocto Project reference distribution (Poky) and an emulated device (QEMU, or the Quick EMUlator); however, the steps below are tailored to the SOM (and DVK) and assume that you have successfully setup a development PC for use with the Yocto Project (see the [system requirements](https://docs.yoctoproject.org/ref-manual/system-requirements.html#system-requirements) for more details).


### SOM (and DVK) Prerequisites
In addition to the standard Yocto Project system requirements, the [`repo`](https://gerrit.googlesource.com/git-repo/) tool is needed to support cloning the necessary repositories at the proper revisions via Git, and the steps necessary to install the `repo` tool are listed in the ['Install'](https://gerrit.googlesource.com/git-repo/#install) section of the homepage.

## Build the Default DVK Image
1. Create a working directory for the Yocto build environment and change directories to it:

    ```bash
    mkdir -p <working-dir>
    cd <working-dir>
    ```

2. Pull in the necessary meta data sources using the `repo` tool optionally replacing `<branch>` with the target branch and `<manifest>` with the target manifest file:
    
    ```bash
    repo init -u git@github.com:Ezurio/Summit-SOM-Release-Packages.git [-b <branch>] [-m <manifest>]
    repo sync
    ```

3. Setup the Yocto build environment by sourcing the `setup-environment` script and passing the proper parameters.

    ```bash
    [MACHINE=<machine>] [DISTRO=<distro>] source setup-environment-xxx [-h] <build-dir>
    ```

    | Parameter     | Description |
    |---------------|-------------|
    | `<machine>`   | Machine name - there are supported machines are Summit SOM 8MP DVK (imx8mp-summitsom), for the Carbon AM62 (carbon-am62)|
    | `<distro>`    | Distro name - there are currently three supported distro names for the DVK:<ul><li>`summitsom-cmd`</li><li>`summitsom-wayland` (default)</li><li>`summitsom-xwayland`</li></ul> |
    | `-h`          | Show the script usage help info (optional) |
    | `<build-dir>` | Build directory |

    For example, to setup the build environment for the DVK with Wayland support and use a build directory of `build`, use the following command:

    ```bash
    MACHINE=carbon-am62 DISTRO=summitsom-wayland source setup-environment-xxx build
    ```

    **Note:** The first time the `setup-environment` script is run, the NXP Yocto BSP EULA is shown and must be read and accepted before continuing with development. On subsequent runs of the script, you do not need to specify the `MACHINE` and `DISTRO` parameters or accept the EULA. The settings can be specified at any time in the `<build-dir>/conf/local.conf` file.

4. Build the image with `bitbake`. Presently, there are two supported images that can be built for the SOM (and DVK):

    | Image Name            | Description                                                      |
    |-----------------------|------------------------------------------------------------------|
    | `image-summitsom-cmd` | command line image                            |

    To start the build, use the following command replacing `<image-name>` with the target image name:
    ```bash
    bitbake <image-name>
    ```

    For example, to build the command line image, use:

    ```bash
    bitbake image-summitsom-cmd
    ```

5. Wait for the build to complete and find the build artifacts. The build process can take a significant amount of time, and when complete, the build artifacts are available in the following directory:
    ```
    <build-dir>/tmp/deploy/images/<machine-name>
    ```

    Here, you will find a `.swu` file that can be used to securely update the that uses the following naming pattern:
    ```
    <image-name>-<machine-name>-<date-time>.swu
    ```

    For example, the command line image would have a `.swu` file name like:
    ```
    image-summitsom-cmd-XXXXX-summitsom-20220324205149.swu
    ```

6. There are a number of ways to flash the onboard eMMC:

    * HTTP/FTP Server

        Start a HTTP or FTP server on a development machine that is connected to a network which can be accessed by the Summit SOM.

        Boot the DVK from the SD card and use the `fw_update` script (built around `swupdate`) to flash the image to both a/b sides:
        ```
        fw_update -m complete <url>
        poweroff
        ```

    * USB Flash Drive

        Copy the `.swu` file onto a USB flash drive.

        Boot the DVK from SD card and use the `fw_update` script (built around `swupdate`) to flash the image to both a/b sides:
        ```
        fw_update -m complete /media/.../update.swu
        poweroff
        ```

    * SD Card

        Copy the `.swu` file onto a premade DVK SD card:
        ```
        sudo cp image-summitsom-cmd-XXXXX-summitsom-20220324205149.swu /media/$USER/rootfs_data/upper/home/root/ && sync
        ```
        Boot the DVK from the SD card and use the `fw_update` script (built around `swupdate`) to flash the image to both a/b sides:
        ```
        fw_update </path/to/update.swu | url/to/swu>
        poweroff
        ```

	Change the boot mode select DIP switch positions from 0011 to 0010 to switch to internal eMMC and cycle power. The board should now boot from the Summit SOM' onboard eMMC.

## Updating the Image While Running From eMMC
The standard DVK image utilizes an A/B update scheme when running from onboard eMMC which allows for built-in fallback support in the event of an update failure. To perform an image update, use one of the methods below once you have created a new `.swu` update file.

* HTTP/FTP Server

    Start a HTTP or FTP server on a development machine that is connected to a network which can be accessed by the SOM DVK.

    Boot the DVK from internal eMMC and use the `fw_update` script (built around `swupdate`) to flash the image. The script will automatically determine where flash the new image (i.e., side 'b' if running from side 'a' or side 'a' if running from side 'b'), toggle the proper environment variable to boot from the new bootside and reboot the board:
    ```
    fw_update <url>
    ```

* USB Flash Drive

    Copy the `.swu` file onto a USB flash drive.

    Boot the DVK from internal eMMC and use the `fw_update` script (built around `swupdate`) to flash the image. The script will automatically determine where flash the new image (i.e., side 'b' if running from side 'a' or side 'a' if running from side 'b'), toggle the proper environment variable to boot from the new bootside and reboot the board:
    ```
    fw_update /media/.../update.swu
    ```
## Next Steps/Customizing the Image
See the links below from the Yocto Project documentation site for further information and in depth guides to customize your image and integrate the SOM into your custom design:

* [What I wish I’d known about Yocto Project](https://docs.yoctoproject.org/what-i-wish-id-known.html)
* [Transitioning to a custom environment for systems development](https://docs.yoctoproject.org/transitioning-to-a-custom-environment.html)
* [Yocto Project Software Overview](https://www.yoctoproject.org/software-overview/)
* [Tips and Tricks Wiki](https://wiki.yoctoproject.org/wiki/TipsAndTricks)

## TI BSP special notes

TI BSP contains main DVK and overlay device trees. 
Overlay adds following options:
* LVDS port D to HDMI adapter (lvds-d-hdmi)
* LVDS port E to HDMI adapter (lvds-e-hdmi) (it can only mirror port D)
* RGB to HDMI adapter (rgb-hdmi)
* RGB to LVDS adapter with (rgb-lvds)
* Wi-Fi on M.2 slot - LWBxx/IFxx radio (m2-lwb-if)
* Wi-Fi on M.2 slot - NX611 radio (m2-nx611)
* Wi-Fi on M.2 slot - TI351 radio (m2-cc33xx)
* Wi-Fi on M.2 slot - 60 radio (m2-60)

To enable overlay execute command as following:
```
set-mode lvds-d-hdmi
```
