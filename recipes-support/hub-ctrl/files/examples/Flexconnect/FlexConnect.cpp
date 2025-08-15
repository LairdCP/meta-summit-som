/*
**********************************************************************************
* © 2015 Microchip Technology Inc. and its subsidiaries.
* You may use this software and any derivatives exclusively with
* Microchip products.
* THIS SOFTWARE IS SUPPLIED BY MICROCHIP "AS IS".
* NO WARRANTIES, WHETHER EXPRESS, IMPLIED OR STATUTORY, APPLY TO THIS SOFTWARE,
* INCLUDING ANY IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY,
* AND FITNESS FOR A PARTICULAR PURPOSE, OR ITS INTERACTION WITH MICROCHIP
* PRODUCTS, COMBINATION WITH ANY OTHER PRODUCTS, OR USE IN ANY APPLICATION.
* IN NO EVENT WILL MICROCHIP BE LIABLE FOR ANY INDIRECT, SPECIAL, PUNITIVE,
* INCIDENTAL OR CONSEQUENTIAL LOSS, DAMAGE, COST OR EXPENSE OF ANY KIND
* WHATSOEVER RELATED TO THE SOFTWARE, HOWEVER CAUSED, EVEN IF MICROCHIP HAS
* BEEN ADVISED OF THE POSSIBILITY OR THE DAMAGES ARE FORESEEABLE.
* TO THE FULLEST EXTENT ALLOWED BY LAW, MICROCHIP'S TOTAL LIABILITY ON ALL
* CLAIMS IN ANY WAY RELATED TO THIS SOFTWARE WILL NOT EXCEED THE AMOUNT OF
* FEES, IF ANY, THAT YOU HAVE PAID DIRECTLY TO MICROCHIP FOR THIS SOFTWARE.
* MICROCHIP PROVIDES THIS SOFTWARE CONDITIONALLY UPON YOUR ACCEPTANCE
* OF THESE TERMS.
**********************************************************************************
*  FlexConnect.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1.1 $  $DateTime: 2015/09/21  18:04:00 $  $    $
*  Description: Sample Application for Flexconnect demo
**********************************************************************************
* $File:  
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <iostream>

#include <cstdlib>
// PT2 SDK API Header file
#include "MchpUSBInterface.h"

using namespace std;

libusb_context *ctx1 = NULL; 

int main (int argc, char* argv[])
{
	CHAR sztext[2048];
	UINT hubindex = 0 ;
	DWORD dwError = 0;
	WORD vendor_id  = 0x0424, product_id =0x4504; // USB 4604 and USB 84604 pid is 0x4504
	HANDLE hDevice =  INVALID_HANDLE_VALUE;
	int cnt = 0,error=0;
	libusb_device **devs;
	libusb_device_descriptor desc;

	// Get the version number of the SDK
	if (FALSE == MchpUsbGetVersion(sztext))
	{
		printf ("\nError:SDK Version cannot be obtained,Press any key to exit....");
		exit (1);
	}

	cout << "SDK Version:" <<sztext << endl;

	printf("FlexConnect Demo\n");

	if(argc < 3)
	{
		printf("PID and VID are not mentioned.\nUSAGE: flexconenct vid pid\n");
		printf("Taking default values: 0x0424 0x4504\n"); // USB 4604 and USB 84604 pid is 0x4504
		printf("ctrl+c to close the application\n");
		sleep(5);

	}
	else
	{
		vendor_id = strtol (argv[1], NULL, 0) ;
		product_id = strtol (argv[2], NULL, 0) ;
	}

	printf ("VID:PID %04x:%04x\n", vendor_id, product_id);	
	
	hDevice = MchpUsbOpenID(vendor_id,product_id);

	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\n MchpUsbOpenID Failed:\n");
		exit (1);
	}

	printf ("MchpUsbOpenID successful...\n");	

	printf ("Enabling Flex on the MCHP Hub\n");

	// Refer to Table 1 of APP note AN1700 http://ww1.microchip.com/downloads/en/AppNotes/00001700A.pdf for more details
	// 0x8041 is the wValue referred to in Table 2 of the APP note ; it means Bit 6 =FLexConnect =1 after the hub is re-attached

	if(FALSE == MchpUsbFlexConnect(hDevice, 0x8041)) 
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf ("MchpUsbFlexConnect Failed:Error Code:%04x\n",(unsigned int)dwError);
		exit (1);
	}

	// This is expected to fail because after the Flexconnect command , the Linux host would have lost control of the hub
	if(FALSE == MchpUsbClose(hDevice))
	{
		dwError = MchpUsbGetLastErr((HANDLE)hubindex);
		printf ("MchpUsbClose:Error Code,%04x\n",(unsigned int)dwError);
		exit (1);
	}
	sleep(3);
	//Check Flexconnect was success
	error = libusb_init(&ctx1);
	if(error < 0)
	{
		printf("MCHP_Error_LibUSBAPI_Fail: Initialization LibUSB failed\n");
		exit(1);;
	}

	cnt = libusb_get_device_list(ctx1, &devs);
	if(cnt < 0) 
	{
		printf("Failed to get the device list \n");
		exit (1);
	}
	for (int i = 0; i < cnt; i++) 
	{
		libusb_device *device = devs[i];
		
		error = libusb_get_device_descriptor(device, &desc);
		if(error != 0)
		{
			printf("LIBUSB_ERROR: Failed to retrieve device descriptor for device[%d] \n", i);
		}
		if(desc.idVendor == vendor_id && desc.idProduct == product_id)
		{
			printf("Flexconnect Failed \n");
			exit(1);
		}
	}
	printf("Flexconnect success \n");
	

	return 0;
}

