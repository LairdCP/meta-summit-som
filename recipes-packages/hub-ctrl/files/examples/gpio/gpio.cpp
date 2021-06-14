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
*  gpio.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1.1 $  $DateTime: 2015/09/21  18:04:00 $  $    $
*  Description: Sample code for GPIO Bridging
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

int main (int argc, char* argv[])
{
	CHAR sztext[2048];
	DWORD dwError = 0;
	WORD vendor_id = 0x424 ,product_id= 0x4504;
	HANDLE hDevice =  INVALID_HANDLE_VALUE;
	int PIONumber=10;
   	int PINState=1;
	BYTE byOperation;
	// Get the version number of the SDK
	if (FALSE == MchpUsbGetVersion(sztext))
	{
		printf ("\nError:SDK Version cannot be obtained,Press any key to exit....");
		exit (1);
	}

	cout << "SDK Version:" <<sztext << endl;
	memset(sztext,0,2048);

	printf("GPIO Demo\n");

	if(argc < 1)
	{
		printf("ERROR: Invalid Usage.\n");
		printf("Use --help option for further details \n");
		exit (1);
	}
	else if((0 == strcmp(argv[1],"--help")) || (0 == strcmp(argv[1],"/?")))
	{
		printf("Operation : Get PIO PinState \n");
		printf("Usage :./gpio VID(Hex) PID(Hex) Operation(0x00) PIONumber \n");
		printf("Example :./gpio 0x0424 0x4504 0x00 10 \n\n");
		printf("Operation : Set PIO PinState \n");
		printf("Usage :./gpio VID(Hex) PID(Hex) Operation(0x01) PIONumber PIOPinState\n");
		printf("Example :./gpio 0x0424 0x4504 0x01 10 0 \n\n");
		exit(1);
	}
	else
	{
		vendor_id  =  strtol (argv[1], NULL, 0) ;
		product_id =  strtol (argv[2], NULL, 0) ;
		byOperation  =  strtol (argv[3], NULL, 0) ;
		PIONumber  = strtol (argv[4], NULL, 0) ;
		if(byOperation == 0x01) //set
		{
			PINState = strtol (argv[5], NULL, 0);
		}
	}

	printf ("VID:PID %04x:%04x PIO=%d\n", vendor_id, product_id, PIONumber);	
	
	hDevice = MchpUsbOpenID(vendor_id,product_id);

	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\nError: MchpUsbOpenID Failed:\n");
		exit (1);
	}

	printf ("MchpUsbOpenID successful...\n");	

	if (FALSE ==MchpUsbConfigureGPIO (hDevice, PIONumber))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("Error: MchpUsbConfigureGPIO- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	if(byOperation == 0x01) //set
	{
		if (FALSE == MchpUsbGpioSet (hDevice,PIONumber,PINState))
		{

			dwError = MchpUsbGetLastErr(hDevice);
			printf("Error: MchpUsbGpioSet- %04x\n",(unsigned int)dwError);
			exit (1);

		}
	}
	else if(byOperation == 0x00) //get
	{
		if (FALSE == MchpUsbGpioGet (hDevice,PIONumber,(UINT8 *)&PINState))
		{

			dwError = MchpUsbGetLastErr(hDevice);
			printf("Error: MchpUsbGpioSet- %04x\n",(unsigned int)dwError);
			exit (1);

		}
		printf("Pin State is %d \n", PINState);
	}
	//Close device handle
	if(FALSE == MchpUsbClose(hDevice))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf ("MchpUsbClose:Error Code,%04x\n",(unsigned int)dwError);
		exit (1);
	}

	return 0;
}

