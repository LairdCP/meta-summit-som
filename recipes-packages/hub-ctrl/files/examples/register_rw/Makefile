
# Choose the project
# Generates  register_rw

PROJECT = register_rw


######################################################
############## For Flex demo #########################
EXE_OUT = register_rw

CPP_SRC = register_rw.cpp 

#Include Paths
INC_DIR = ../../libpt2
LIB_DIR = ../../libpt2

#Defnes
DEFINES = -D LIBUSB

#
# DO NOT EDIT BELOW
#

# Temporary directories
OBJ_DIR = ./obj
OUT_DIR = ./out

# Automatic generation of some important lists
INC_PATH = $(foreach TMP,$(INC_DIR),-I$(TMP))
LIB_PATH = $(foreach TMP,$(LIB_DIR),-L$(TMP))

OBJS = $(CPP_SRC:.cpp=.o) $(GCC_SRC:.c=.o)

# Libraries
LIBS = -pthread -lm -ldl -lpt2 $(shell pkg-config --libs libusb-1.0)
 
# Flags
CCFLAGS = -g -Wall $(shell pkg-config --cflags libusb-1.0)
 
# Default targets
default: $(EXE_OUT) $(LIB_OUT)

.cpp.o:
	@mkdir -p ./obj
	@echo "============="
	@echo "Compiling $<"
	@$(CXX) $(INC_PATH) $(CXXFLAGS) $(CCFLAGS) $(DEFINES) -c $< -o $(OBJ_DIR)/$(notdir $@)
 
.c.o:
	@mkdir -p ./obj
	@echo "============="
	@echo "Compiling $<"
	@$(CC) $(INC_PATH) $(CFLAGS) $(CCFLAGS) $(DEFINES) -c $< -o $(OBJ_DIR)/$(notdir $@)

$(EXE_OUT): $(OBJS)
	@echo "============="
	@mkdir -p ./out
	@echo "Creating $(EXE_OUT)"
	$(CXX)  $(addprefix $(OBJ_DIR)/, $(notdir $(OBJS))) $(LDFLAGS) $(LIB_PATH) $(LIBS) -o $(addprefix $(OUT_DIR)/, $(EXE_OUT))

$(LIB_OUT): $(OBJS)
	@echo "============="
	@mkdir -p ./out	
	@echo "Creating $(LIB_OUT)"
	@$(AR) rcs $(addprefix $(OUT_DIR)/, $(LIB_OUT)) $(addprefix $(OBJ_DIR)/, $(notdir $(OBJS)))

find:
	-exec touch \{\} \;

clean:
	rm -rf $(OBJ_DIR)
	rm -rf $(OUT_DIR)
	rm -f Makefile.bak
