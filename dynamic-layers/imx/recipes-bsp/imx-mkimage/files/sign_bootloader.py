"""
This script signs the bootloader using the signing server.
"""

import sys
import getopt
import json

USAGE_TEXT = (
    "sign_bootloader.py -u <server-url> -p <part-number> -o <signing-offsets>"
    " -f <unsigned-flash> -s <signed-flash> -a <access-token> -i <auth-client-id>"
    "-t <auth-authority> -k <auth-client-secret> -q <auth-scopes>"
)


def main(argv):
    """
    Main function to sign the bootloader using the signing server.
    """

    server_url: str = None
    part_number: str = None
    signing_offsets: str = None
    unsigned_flash_path: str = None
    signed_flash_path: str = None
    access_token: str = None
    auth_client_id: str = None
    auth_authority: str = None
    auth_client_secret: str = None
    auth_scopes: str = None

    try:
        opts, _ = getopt.getopt(
            argv,
            "hu:p:o:f:s:a:i:t:k:q:",
            [
                "server-url=",
                "part-number=",
                "signing-offsets=",
                "unsigned-flash=",
                "signed-flash=",
                "access-token=",
                "auth-client-id=",
                "auth-authority=",
                "auth-client-secret=",
                "auth-scopes=",
            ],
        )
    except getopt.GetoptError:
        print(USAGE_TEXT)
        sys.exit(2)

    for opt, arg in opts:
        if opt == "-h":
            print(USAGE_TEXT)
            sys.exit()
        elif opt in ("-u", "--server-url"):
            server_url = arg
        elif opt in ("-p", "--part-number"):
            part_number = arg
        elif opt in ("-o", "--signing-offsets"):
            signing_offsets = arg
        elif opt in ("-f", "--unsigned-flash"):
            unsigned_flash_path = arg
        elif opt in ("-s", "--signed-flash"):
            signed_flash_path = arg
        elif opt in ("-a", "--access-token"):
            access_token = arg
        elif opt in ("-i", "--auth-client-id"):
            auth_client_id = arg
        elif opt in ("-t", "--auth-authority"):
            auth_authority = arg
        elif opt in ("-k", "--auth-client-secret"):
            auth_client_secret = arg
        elif opt in ("-q", "--auth-scopes"):
            auth_scopes = arg

    if not server_url:
        raise ValueError("server_url is not set")

    if not part_number:
        raise ValueError("part_number is not set")

    if access_token:
        print("Using access token from environment")
    else:
        print("Acquiring access token from Azure AD")

        import msal

        app = msal.ConfidentialClientApplication(
            auth_client_id,
            authority=auth_authority,
            client_credential=auth_client_secret,
        )

        result = None
        result = app.acquire_token_silent([auth_scopes], account=None)

        if not result:
            result = app.acquire_token_for_client(scopes=[auth_scopes])

        if "access_token" not in result:
            raise Exception(f"Failed to acquire access token: {result['error']}")

        access_token = result["access_token"]

    # Read the signing offsets and print the details
    signing_offsets_json = json.loads(signing_offsets)
    print(json.dumps(signing_offsets_json, indent=4))

    import requests

    with open(unsigned_flash_path, "rb") as unsignedFlashBin:
        resp = requests.post(
            server_url,
            headers={
                "Authorization": f"Bearer {access_token}",
            },
            data=dict({"destPartNum": part_number}, **signing_offsets_json),
            files={"unsignedFlashBin": unsignedFlashBin},
            timeout=300,
        )

    if resp.status_code != 200:
        raise Exception(f"Failed to sign bootloader: {resp.status_code}")

    with open(signed_flash_path, "wb") as signedFlashBin:
        signedFlashBin.write(resp.content)

    print(f"Signed bootloader saved to: {signed_flash_path}")


if __name__ == "__main__":
    main(sys.argv[1:])
