# Project Name = pt2
# Generates  libpt2.a

PROJECT = Protouch2SDKV1_1

LIB_OUT = libpt2.a
SHL_OUT = libpt2.so

CPP_SRC =   MchpUSBInterface.cpp 
CPP_SRC1 = USB2530_SpiFlash.cpp
CPP_SRC2 = USBHubAbstraction.cpp
         
LIB_USB_PATH=$(shell pkg-config --cflags libusb-1.0)
$(info $(LIB_USB_PATH))
#Include Paths
#INC_DIR = /usr/local/include/libusb-1.0 /usr/local/include/ .
           
#LIB_DIR = /usr/local/lib

#Defnes
DEFINES = -D LIBUSB

#
# DO NOT EDIT BELOW
#

# Temporary directories
OBJ_DIR = .
OUT_DIR = .

# Automatic generation of some important lists
INC_PATH = $(foreach TMP,$(INC_DIR),-I$(TMP))
LIB_PATH = $(foreach TMP,$(LIB_DIR),-L$(TMP))

OBJS = $(CPP_SRC:.cpp=.o) $(GCC_SRC:.c=.o) $(CPP_SRC1:.cpp=.o) $(CPP_SRC2:.cpp=.o)

# Libraries
#LIBS = -lusb-1.0 -lpthread -lm -ldl -lrt -lncurses 
 
# Flags
CFLAGS += $(LIB_USB_PATH) #$(shell pkg-config --libs libusb-1.0)
CXXFLAGS += -std=c++11 -static $(LIB_USB_PATH)
# Compiler

all : $(LIB_OUT)

.cpp.o:
	@mkdir -p ./obj
	@echo "============="
	@echo "Compiling $<"
	@$(CXX) $(INC_PATH) $(CXXFLAGS) $(DEFINES) -c $< -o $(OBJ_DIR)/$(notdir $@)
 
.c.o:
	@mkdir -p ./obj
	@echo "============="
	@echo "Compiling $<"
	@$(CC) $(INC_PATH) $(CFLAGS) $(DEFINES) -c $< -o $(OBJ_DIR)/$(notdir $@)

$(LIB_OUT): $(OBJS)
	@echo "============="
	@mkdir -p ./out	
	@echo "Creating $(LIB_OUT)"
	@ar rcs $(addprefix $(OUT_DIR)/, $(LIB_OUT)) $(addprefix $(OBJ_DIR)/, $(notdir $(OBJS)))

$(SHL_OUT): $(OBJS)
	@echo "============="
	@mkdir -p ./out	
	@echo "Creating $(SHL_OUT)"
	@$(CCC) $(addprefix $(OBJ_DIR)/, $(notdir $(OBJS))) $(LIBS) $(LIB_PATH) -shared -o $(addprefix $(OUT_DIR)/, $(SHL_OUT))
	
find:
	-exec touch \{\} \;
clean:
	rm -f *.o
	rm -f *.a
	rm -f Makefile.bak
