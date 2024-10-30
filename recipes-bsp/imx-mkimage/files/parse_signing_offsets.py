"""
This script parses the compile log for the bootloader signing details.
"""

import json
import sys
import getopt

USAGE_TEXT = "parse_signing_offsets.py -c <compile-log>"


def main(argv):
    """
    Main function to parse the compile log for the bootloader signing details.
    """

    compile_log_path: str = None
    try:
        opts, _ = getopt.getopt(argv, "hc:", ["compile-log="])
    except getopt.GetoptError:
        print(USAGE_TEXT)
        sys.exit(2)

    for opt, arg in opts:
        if opt == "-h":
            print(USAGE_TEXT)
            sys.exit()
        elif opt in ("-c", "--compile-log"):
            compile_log_path = arg

    if not compile_log_path:
        raise ValueError("compile_log is not set")

    spl_hab_block = None
    spl_hab_block_addr = None
    spl_hab_block_off = None
    spl_hab_block_len = None
    sld_hab_block = None
    sld_hab_block_addr = None
    sld_hab_block_off = None
    sld_hab_block_len = None
    spl_csf_off = None
    fit_csf_off = None
    fit_image_data_blocks = []

    # Read the compile log
    with open(compile_log_path, "r", encoding="utf-8") as f:
        compile_log_lines = f.readlines()

    # Parse the compile log for the bootloader signing details
    for line in compile_log_lines:
        if "spl hab block" in line:
            spl_hab_block = line.split("spl hab block:")[1].strip()
            spl_hab_block_addr, spl_hab_block_off, spl_hab_block_len = (
                spl_hab_block.split(" ")
            )
        if "sld hab block" in line:
            sld_hab_block = line.split("sld hab block:")[1].strip()
            sld_hab_block_addr, sld_hab_block_off, sld_hab_block_len = (
                sld_hab_block.split(" ")
            )
        if "csf_off" in line:
            spl_csf_off = line.split("csf_off")[1].strip()
        if "sld_csf_off" in line:
            fit_csf_off = line.split("sld_csf_off")[1].strip()

    fit_image_data_blocks = []
    found_start_line = False
    for line in compile_log_lines:
        if found_start_line:
            if "DEBUG:" in line:
                break

            block = line.strip()
            fit_image_data_blocks.append(
                {
                    "addr": block.split(" ")[0],
                    "off": block.split(" ")[1],
                    "len": block.split(" ")[2],
                }
            )
        else:
            if "print_fit_hab.sh" in line:
                found_start_line = True

    if not (
        spl_hab_block
        and spl_hab_block_addr
        and spl_hab_block_off
        and spl_hab_block_len
        and sld_hab_block
        and sld_hab_block_addr
        and sld_hab_block_off
        and sld_hab_block_len
        and spl_csf_off
        and fit_csf_off
        and len(fit_image_data_blocks) > 0
    ):
        raise Exception(
            "Failed to parse compile log for required bootloader signing details"
        )

    print(
        json.dumps(
            {
                "fitImageDataBlocks": json.dumps(fit_image_data_blocks),
                "splHabBlock": json.dumps(
                    {
                        "addr": spl_hab_block_addr,
                        "off": spl_hab_block_off,
                        "len": spl_hab_block_len,
                    }
                ),
                "sldHabBlock": json.dumps(
                    {
                        "addr": sld_hab_block_addr,
                        "off": sld_hab_block_off,
                        "len": sld_hab_block_len,
                    }
                ),
                "splCsfOff": spl_csf_off,
                "fitCsfOff": fit_csf_off,
            }
        )
    )


if __name__ == "__main__":
    main(sys.argv[1:])
