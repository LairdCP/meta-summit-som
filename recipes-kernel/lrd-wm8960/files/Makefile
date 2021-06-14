KVER = $(shell uname -r)
KSRC ?= /lib/modules/$(KVER)/build
INSTALL_MOD_PATH ?= /lib/modules/$(KVER)/kernel/drivers/iio/adc

obj-m += lrd-wm8960.o

modules:
	$(MAKE) -C $(KSRC) M=$(CURDIR) modules

install: modules_install

modules_install:
	$(MAKE) -C $(KSRC) M=$(CURDIR) INSTALL_MOD_PATH=$(INSTALL_MOD_PATH) modules_install
	
clean:
	$(MAKE) -C $(KSRC) M=$(CURDIR) INSTALL_MOD_PATH=$(INSTALL_MOD_PATH) clean
