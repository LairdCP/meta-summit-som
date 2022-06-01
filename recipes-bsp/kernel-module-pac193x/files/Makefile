KERNEL_SRC ?= /lib/modules/$(shell uname -r)/build

obj-m += pac193x.o

all:
	$(MAKE) -C $(KERNEL_SRC) M=$(CURDIR) modules

install: modules_install

modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$(CURDIR) modules_install

clean:
	@rm -f modules.order Module.symvers *.o *.ko *.mod*
