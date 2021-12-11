#!/usr/bin/env python3
#
# Serial port echo test
#
import sys
import os
import platform
import argparse
import serial
import serial.rs485

def open_port(port, baudrate, mode, pkt_timeout):
    print('Opening serial port {} @ {} bps, timeout = {}'.format(port, baudrate, pkt_timeout))
    s = serial.Serial(port, baudrate, timeout=pkt_timeout)
    # Workaround for a bug in pySerial linux, always clear RS485 settings
    if platform.machine().lower().startswith('arm'):
        s._set_rs485_mode(None)
    if mode == 0:
        # RS-232 mode, use default RS-232 settings
        print('Serial mode: RS-232')
    elif mode == 1:
        print('Serial mode: RS-485 half duplex')
        # For half duplex, set RTS to 1/True on TX.
        # NOTE: Due to a bug in pySerial 3.1.0, we need to set the delays in RS-485 options,
        # but CANNOT set these on Windows (since it's not supported).
        if sys.platform == 'win32':
            rs485_opts = serial.rs485.RS485Settings(rts_level_for_tx=True,rts_level_for_rx=False)
        else:
            rs485_opts = serial.rs485.RS485Settings(rts_level_for_tx=True,rts_level_for_rx=False,delay_before_tx=0,delay_before_rx=0)
        s.rs485_mode = rs485_opts
    else:
        print('Serial mode: RS-485 full duplex')
        # RS485 does not seem to work here, so set RTS explicitly; note that it is inverted, so 'False' sets it High
        s.rts = False
    return s

def test_echo(port, baudrate, mode, pkt_timeout, pkt_len):
    s = open_port(port, baudrate, mode, pkt_timeout)
    print('Echo mode: Awaiting packets (len={})...'.format(pkt_len))
    npkt = 0
    do_more = True
    while do_more:
        pkt = s.read(pkt_len)
        if pkt and len(pkt) == pkt_len:
            s.write(pkt)
            npkt = npkt + 1
            if npkt % 10 == 0:
                print('Echoed {} packets...'.format(npkt))
        else:
            print('Failed to read packet.')
            do_more = False
    print('Test complete, echo sent {} packets.'.format(npkt))

def test_send(port, baudrate, mode, pkt_timeout, pkt_len, pkt_count):
    s = open_port(port, baudrate, mode, pkt_timeout)
    print('Transmit mode: Sending {} packets (len={})...'.format(pkt_count, pkt_len))
    npkt = 0
    while pkt_count == 0 or npkt < pkt_count:
        pkt_write = os.urandom(pkt_len)
        s.write(pkt_write)
        pkt_read = s.read(pkt_len)
        if pkt_read and len(pkt_read) == pkt_len:
            if pkt_read == pkt_write:
                npkt = npkt + 1
                if npkt % 10 == 0:
                    print('Sent {} packets...'.format(npkt))
            else:
                print('Received packet mismatch, TEST FAILED.')
                return
        elif pkt_timeout > 0:
            print('Failed to read complete packet, TEST FAILED.')
            return
        else:
            npkt = npkt + 1
            if npkt % 100 == 0:
                print('Sent {} packets...'.format(npkt))
    print('Successfully sent & received {} packets, TEST PASSED.'.format(npkt))

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('port', help='Port (e.g., /dev/ttyS2)')
    parser.add_argument('baudrate', help='Baud rate (e.g., 115200)')
    parser.add_argument('mode', type=int, help='Serial mode, 0 = RS-232, 1 = RS-485 half duplex, 2 = RS-485 full duplex')
    parser.add_argument('--echo', action='store_true', help='Echo mode (otherwise transmit mode)')
    parser.add_argument('--noecho', action='store_true', help='No echo mode (send only, ignore responses)')
    parser.add_argument('--length', type=int, default=16, help='Packet length')
    parser.add_argument('--count', type=int, default=100, help='Message count (for transmit mode)')
    parser.add_argument('--timeout', type=int, default=30, help='Message timeout')
    args = parser.parse_args()
    if args.echo:
        test_echo(args.port, args.baudrate, args.mode, args.timeout, args.length)
    elif args.noecho:
        test_send(args.port, args.baudrate, args.mode, 0, args.length, 0)
    else:
        test_send(args.port, args.baudrate, args.mode, args.timeout, args.length, args.count)

if __name__ == "__main__":
    main()
