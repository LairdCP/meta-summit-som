/*
**********************************************************************************
* © 2014 Microchip Technology Inc. and its subsidiaries.
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
*   uart_bridging.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1 $  $DateTime: 2014/10/20  18:04:00 $  $    $
*  Description: initial version
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
	CHAR chText[256];
	DWORD dwError = 0;
	WORD vendor_id = 0x424 ,product_id= 0x4504;
	HANDLE hDevice =  INVALID_HANDLE_VALUE;
	UINT32 dwBaudRate,dwLength;
	UINT8 byDataUART[10];
	UINT8 byDataReceive[5];
	
	// Get the version number of the SDK
	if (FALSE == MchpUsbGetVersion(sztext))
	{
		printf ("\nError:SDK Version cannot be obtained,Press any key to exit....");
		exit (1);
	}

	cout << "SDK Version:" <<sztext << endl;
	memset (sztext,0,2048);
	printf("UART Bridging Demo\n");
	
	if(argc < 1)
	{
		printf("ERROR: Invalid Usage.\n");
		printf("Use --help option for further details \n");
		exit (1);
	}
	else if((0 == strcmp(argv[1],"--help")) || (0 == strcmp(argv[1],"/?")))
	{
	
		printf("Usage: ./uartBridging VID PID BaudRate Length Data\n");
		printf("Example: ./uartBridging 0x0424 0x4504 9600 4 60 61 62 62 \n \n");
		exit(1);

	}
	else
	{
		vendor_id  =  strtol (argv[1], NULL, 0) ;
		product_id =  strtol (argv[2], NULL, 0) ;
		dwBaudRate =  strtol (argv[3], NULL, 0) ;
		dwLength   =  strtol (argv[4], NULL, 0) ;
		for (UINT8 i=0; i < dwLength; i++)
		{
			byDataUART[i]= strtol(argv[5+i],0,16);
		}
		
	}
	hDevice = MchpUsbOpenID(vendor_id,product_id);

	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\nError: MchpUsbOpenID Failed:\n");
		exit (1);
	}

	printf ("MchpUsbOpenID successful...\n");

	if(FALSE == MchpUsbEnableUARTBridging(hDevice, TRUE))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("FAILED to Enable UART- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	if(FALSE == MchpUsbSetUARTBaudrate(hDevice, dwBaudRate) )
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("FAILED to set Baud Rate- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	//Transfer data through serial port to the connected serial peripheral
	if(FALSE == MchpUsbUartWrite(hDevice,dwLength, &byDataUART[0]))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("UART Write Failed- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	//Receives data through serial port from the connected serial peripheral
	if(FALSE == MchpUsbUartRead(hDevice,dwLength,&byDataReceive[0]))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("UART Read Failed- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	else
	{
		for(UINT8 i =0; i< dwLength; i++)
		{
			sprintf(chText,"%c \t",byDataReceive[i] );
			strcat(sztext,chText);
		}
		printf("%s \n",sztext);
	}

	if(FALSE == MchpUsbEnableUARTBridging(hDevice, FALSE))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("FAILED to Disable UART- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	//Close devic handle
	if(FALSE == MchpUsbClose(hDevice))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf ("MchpUsbClose:Error Code,%04x\n",(unsigned int)dwError);
		exit (1);
	}
	return 0;
}

