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
*  OTP_Programmer.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1.1 $  $DateTime: 2015/09/21 18:04:00 $  $    $
*  Description: sample code for OTP programming
**********************************************************************************
* $File:  
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "MchpUSBInterface.h"
#include "USBHubAbstraction.h"
#include "USB2530_SpiFlash.h"

int main (int argc, char* argv[])
{
	HANDLE hDevice;
	DWORD dwError = 0;
	WORD vendor_id = 0x424 ,product_id= 0x4504;
	
	if(argc<1)
	{
		printf("ERROR: Invalid Usage.\n");
		printf("Use --help option for further details \n");
		exit (1);	
	}
	else if((0 == strcmp(argv[1],"--help")) || (0 == strcmp(argv[1],"/?")))
	{
		printf("Usage: ./OTP_Program VID(Hex) PID(Hex) ConfigFile \n");
		printf("Example: ./OTP_Program 0x0424 0x4504 myConfig.bin \n \n");
		exit(1);
	}
	else
	{
		vendor_id  =  strtol (argv[1], NULL, 0) ;
		product_id =  strtol (argv[2], NULL, 0) ;
	}
	
	/*
	* 1. Open handle to the connected USB2530 device 
	*/
	hDevice = MchpUsbOpenID(vendor_id,product_id);
	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\nError: MchpUsbOpenID Failed:\n");
		exit (1);
	}
	
	printf ("MchpUsbOpenID successful... \n");

	if(FALSE == MchpProgramFile(hDevice ,argv[3]))
	{
		printf("Programming Failed \n");
		dwError = MchpUsbGetLastErr(hDevice);
		printf ("Error,%04xn",(unsigned int)dwError);
		exit (1);
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
