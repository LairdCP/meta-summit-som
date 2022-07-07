#!/usr/bin/python

from setuptools import setup, Extension

swclient_module = Extension('swclient',
        libraries = ['swupdate'],
        sources = ['swclient.c'])

setup(name='swclient',
      version='1.1.0',
      description='swupdate client module written in C',
      ext_modules=[swclient_module])
