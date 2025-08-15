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
*   spi_bridging.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1.1 $  $DateTime: 2015/09/21  18:04:00 $  $    $
*  Description: Sample code for SPI Bridging
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
#include "USBHubAbstraction.h"
#include "USB2530_SpiFlash.h"

using namespace std;

int main (int argc, char* argv[])
{
	CHAR sztext[2048];
	CHAR chText[256];
	DWORD dwError = 0;
	WORD vendor_id = 0x424 ,product_id= 0x4504;
	BYTE byStartAddr,byOperation;
	HANDLE hDevice =  INVALID_HANDLE_VALUE;
	UINT8 byLength;
	uint8_t  pbyBuffer[128 * 1024];
	uint8_t wDataLength;
	BYTE byReadFirmwareData[64 * 1024];
	string sFirmwareFile;
	UINT8 byBuffer;
	UINT16 DataLen;
	UINT32 wTotalLen;
	UINT8 byReadBuffer[512];
	

	// Get the version number of the SDK
	if (FALSE == MchpUsbGetVersion(sztext))
	{
		printf ("\nError:SDK Version cannot be obtained,Press any key to exit....");
		exit (1);
	}

	cout << "SDK Version:" <<sztext << endl;
	memset(sztext,0,2048);

	printf("SPI Bridging Demo\n");
	
	if(argc < 1)
	{
		printf("ERROR: Invalid Usage.\n");
		printf("Use --help option for further details \n");
		exit (1);
	}
	else if((0 == strcmp(argv[1],"--help")) || (0 == strcmp(argv[1],"/?")))
	{
		printf("Operation : Write \n");
		printf("Usage: ./spiBridging VID(Hex) PID(Hex) Operation(0x01) FirmwareFile \n");
		printf("Example: ./spiBridging 0x0424 0x4504 0x01 USB2534_SPI_CARPLAY_V128.bin \n \n");
		printf("Operation : Read \n");
		printf("Usage: ./spiBridging VID(Hex) PID(Hex) Operation(0x00) StartAddress Length \n");
		printf("Example: ./spiBridging 0x0424 0x4504 0x00 0x00 4 \n\n");
		printf("Operation : Transfer\n");
		printf("Usage: ./spiBridging VID(Hex) PID(Hex) Operation(0x03) Command DataLength TotalLength\n");
		printf("Example: ./spiBridging 0x0424 0x4504 0x03 0x9f 1 4 \n\n");
		exit(1);

	}
	else
	{
		vendor_id  =  strtol (argv[1], NULL, 0) ;
		product_id =  strtol (argv[2], NULL, 0) ;
		byOperation=  strtol (argv[3], NULL, 0) ;
		if(byOperation == 0x00) // Read.
		{
			byStartAddr=  strtol (argv[4], NULL, 0) ;
			byLength   =  strtol (argv[5], NULL, 0) ;
		}
		else if(byOperation == 0x01) //Write
		{
			sFirmwareFile = argv[4];
		}
		else if(byOperation == 0x03) //Transfer
		{
			byBuffer = strtol (argv[4],NULL,0);
			DataLen	 = strtol (argv[5],NULL,0);
			wTotalLen = strtol (argv[6],NULL,0);
		}

	}
	
	hDevice = MchpUsbOpenID(vendor_id,product_id);

	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\nError: MchpUsbOpenID Failed:\n");
		exit (1);
	}
	
	printf ("MchpUsbOpenID successful... \n");
	
	if(byOperation == 0x00) //Read
	{	
		if(FALSE == MchpUsbSpiFlashRead(hDevice,byStartAddr, &byReadFirmwareData[0],byLength))
		{
			printf ("\nError: Read Failed:\n");
			exit (1);
		}
		for(UINT8 i =0; i< byLength; i++)
		{
			sprintf(chText,"0x%02x \t",byReadFirmwareData[i] );
			strcat(sztext,chText);
		}
		printf("%s \n",sztext);
	}
	else if(byOperation == 0x01)//Write
	{
		wDataLength = ReadBinfile(sFirmwareFile.c_str(),pbyBuffer);
		if(wDataLength <=0)
		{
			printf("Failed to Read Content of File\n");
			exit (1);
		}
		if(FALSE == MchpUsbSpiFlashWrite(hDevice,byStartAddr, &pbyBuffer[0],0xfffe))
		{
			printf ("\nError: Write Failed:\n");
			exit (1);
		}
	}
	else //Transfer
	{
		if(FALSE == MchpUsbSpiSetConfig (hDevice,1))
		{
			printf ("\nError: SPI Pass thru enter failed:\n");
			exit (1);		
		}
		if(FALSE == MchpUsbSpiTransfer(hDevice,0,&byBuffer,DataLen,wTotalLen)) //write
		{
			printf("SPI Transfer write failed \n");
			exit (1);
		}
		if(FALSE == MchpUsbSpiTransfer(hDevice,1,(UINT8 *)&byReadBuffer,wTotalLen,wTotalLen))
		{
			printf("SPI Transfer read failed \n");
			exit (1);
		}
		for(UINT8 i =0; i< wTotalLen; i++)
		{
			sprintf(chText,"0x%02x \t",byReadBuffer[i] );
			strcat(sztext,chText);
		}
		printf("%s \n",sztext);
		if(FALSE == MchpUsbSpiSetConfig (hDevice,0))
		{
			printf ("\nError: SPI Pass thru enter failed:\n");
			exit (1);		
		}
	}
	//close device handle 
	if(FALSE == MchpUsbClose(hDevice))
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf ("MchpUsbClose:Error Code,%04x\n",(unsigned int)dwError);
		exit (1);
	}
	return 0;
}

