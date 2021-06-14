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
*  i2c_bridging.cpp
*   This file gives the sample code/ test code for using MchpUSB2530 API
*	Interface.
**********************************************************************************
*  $Revision: #1.1 $  $DateTime: 2015/09/21  18:04:00 $  $    $
*  Description: Sample code for i2c bridging demo
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
	BYTE bySlaveAddr,byStartAddr,byOperation;
	HANDLE hDevice =  INVALID_HANDLE_VALUE;
	INT iLength,iClockRate;
	BYTE byWriteData[513];
	BYTE byReadData[512];
	BOOL bStart,bStop,bNack;
	

	// Get the version number of the SDK
	if (FALSE == MchpUsbGetVersion(sztext))
	{
		printf ("\nError:SDK Version cannot be obtained,Press any key to exit....");
		exit (1);
	}

	cout << "SDK Version:" <<sztext << endl;
	memset(sztext,0,2048);

	printf("I2C Bridging Demo\n");
	
	
	if(argc < 1)
	{
		printf("ERROR: Invalid Usage.\n");
		printf("Use --help option for further details \n");
		exit (1);
	}
	else if((0 == strcmp(argv[1],"--help")) || (0 == strcmp(argv[1],"/?")))
	{
		
		printf("SlaveAddr	: I2C Slave Address \n");
		printf("ClockRate	: Interger value as shown below (1,2...)\n");
		printf("                  1 = 62.5Khz\n");
		printf("                  2 = 238Khz\n");
		printf("                  3 = 268Khz\n");
		printf("                  4 = 312Khz\n");
		printf("                  5 = 375Khz\n");
		printf("StartAddr	: Start Address of I2C Slave to Read/Write \n");
		printf("Start		: 1 - Generates Start condition\n");
		printf("		: 0 - Does not generate Start condition \n");
		printf("Stop		: 1 - Generates Stop condition\n");
		printf("		: 0 - Does not generate Stop condition \n");
		printf("Nack		: 1 - Generates NACK condition for the last byte of the transfer\n");
		printf("		: 0 - Does not generate NACK condition \n\n");
		printf("Operation 	: Write \n");
		printf("Usage		: ./i2cBridging VID(Hex) PID(Hex) Operation(0x01) SlaveAddr ClockRate StartAddr Length Data \n");
		printf("Example		: ./i2cBridging 0x0424 0x4504 0x01 0x50 1 0x00 4 0x11 0x22 0x33 0x44 \n \n");
		printf("Operation 	: Read \n");
		printf("Usage		: ./i2cBridging VID(Hex) PID(Hex) Operation(0x00) SlaveAddr ClockRate StartAddr Length \n");
		printf("Example		: ./i2cBridging 0x0424 0x4504 0x00 0x50 1 0x00 4 \n\n");
		printf("Operation	: Transfer: Write \n");
		printf("Usage		: ./i2cBridging VID(Hex) PID(Hex) Operation(0x03) SlaveAddr ClockRate StartAddr Length Start(0/1) Stop(0/1) Nack(0/1) Data\n");
		printf("Example		: ./i2cBridging 0x0424 0x4504 0x03 0x50 1 0x00 4 1 1 0 0x11 0x22 0x33 0x44 \n \n");
		printf("Operation	: Transfer: Read \n");
		printf("Usage		: ./i2cBridging VID(Hex) PID(Hex) Operation(0x04) SlaveAddr ClockRate StartAddr Length Start(0/1) Stop(0/1) Nack(0/1)\n");
		printf("Example		: ./i2cBridging 0x0424 0x4504 0x04 0x50 1 0x00 4 1 1 1\n \n");

		exit(1);

	}
	else
	{
		vendor_id  =  strtol (argv[1], NULL, 0) ;
		product_id =  strtol (argv[2], NULL, 0) ;
		byOperation=  strtol (argv[3], NULL, 0) ;
		bySlaveAddr=  strtol (argv[4], NULL, 0) ;
		iClockRate =  strtol (argv[5], NULL, 0) ;
		byStartAddr=  strtol (argv[6], NULL, 0) ;
		iLength	   =  strtol (argv[7], NULL, 0) ;
		byWriteData[0]= byStartAddr;
		if(byOperation == 0x01) //Write
		{
			for (UINT8 i=1; i <= iLength; i++)
			{
				byWriteData[i] = strtol (argv[7 + i], NULL,0);	
			}
		}
		if(byOperation == 0x03 || byOperation == 0x04) //Transfer
		{
			bStart = strtol (argv[8], NULL,0) ;
			bStop  = strtol (argv[9], NULL,0) ;
			bNack  = strtol (argv[10], NULL,0) ;
			if(byOperation == 0x03 ) //Write
			{
				for (UINT8 i=1; i <= iLength; i++)
				{
					byWriteData[i] = strtol (argv[10 + i], NULL,0);	
				}
			}
		}	
	}
	
	hDevice = MchpUsbOpenID(vendor_id,product_id);

	if(INVALID_HANDLE_VALUE == hDevice) 
	{
		printf ("\nError: MchpUsbOpenID Failed:\n");
		exit (1);
	}

	printf ("MchpUsbOpenID successful...\n");
	
	if(FALSE == MchpUsbI2CSetConfig(hDevice,0,iClockRate))	
	{
		dwError = MchpUsbGetLastErr(hDevice);
		printf("Error: MchpUsbI2CSetConfig- %04x\n",(unsigned int)dwError);
		exit (1);
	}
	if(0x00 == byOperation) // Read
	{
		if(FALSE== MchpUsbI2CWrite(hDevice,1,(BYTE *)&byStartAddr,bySlaveAddr))
		{
			dwError = MchpUsbGetLastErr(hDevice);
			printf("Failed to write- %04x\n",(unsigned int)dwError);
			exit (1);
		}
		if(FALSE == MchpUsbI2CRead(hDevice,iLength,&byReadData[0],bySlaveAddr) )
		{
			dwError = MchpUsbGetLastErr(hDevice);
			printf("Failed to Read- %04x\n",(unsigned int)dwError);
			exit (1);
		}
		for(UINT8 i =0; i< iLength; i++)
		{
			sprintf(chText,"0x%02x \t",byReadData[i] );
			strcat(sztext,chText);
		}
		printf("%s \n",sztext);
	}
	else if(0x01 == byOperation)//Write
	{
		if(FALSE== MchpUsbI2CWrite(hDevice,(iLength+1),(BYTE *)&byWriteData,bySlaveAddr))
		{
			dwError = MchpUsbGetLastErr(hDevice);
			printf("Failed to write- %04x\n",(unsigned int)dwError);
			exit (1);
		}
	}
	else //Transfer
	{
		if(0x03 == byOperation) //Transfer WRite
		{
			if(FALSE ==  MchpUsbI2CTransfer(hDevice,0, (BYTE *)&byWriteData,(iLength+1),bySlaveAddr,bStart,bStop,bNack))
			{
				dwError = MchpUsbGetLastErr(hDevice);
				printf("Failed to write- %04x\n",(unsigned int)dwError);
				exit (1);
			}
		}
		else if(0x04 == byOperation)
		{
			if(FALSE ==  MchpUsbI2CTransfer(hDevice,0, (BYTE *)&byWriteData,1,bySlaveAddr,bStart,bStop,bNack))
			{
				dwError = MchpUsbGetLastErr(hDevice);
				printf("Failed to write start addr- %04x\n",(unsigned int)dwError);
				exit (1);
			}
			if(FALSE ==  MchpUsbI2CTransfer(hDevice,1,&byReadData[0],iLength,bySlaveAddr,bStart,bStop,bNack))
			{
				dwError = MchpUsbGetLastErr(hDevice);
				printf("Failed to Read- %04x\n",(unsigned int)dwError);
				exit (1);
			}
			for(UINT8 i =0; i< iLength; i++)
			{
				sprintf(chText,"0x%02x \t",byReadData[i] );
				strcat(sztext,chText);
			}
			printf("%s \n",sztext);
		}
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

