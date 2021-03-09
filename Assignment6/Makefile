TARGET := main
SRC_DIR := src
MY_PROJECT_BASE_DIRECTORY := /home/kryvokhyzha/IdeaProjects/parallel-and-distributed-computing/Assignment6
SRC_DIR := $(MY_PROJECT_BASE_DIRECTORY)/src
BUILD_DIR := $(MY_PROJECT_BASE_DIRECTORY)/build

init:
	if [ -d $(BUILD_DIR) ]; then \
  		echo ; \
  	else \
  	  mkdir $(BUILD_DIR); \
  	fi && \
  	if [ -d $(SRC_DIR) ]; then \
  		echo ; \
    else \
  		mkdir $(SRC_DIR); \
  	fi
build_main:
	mpic++ -o $(BUILD_DIR)/$(TARGET).o $(SRC_DIR)/$(TARGET).cpp
run:
	mpirun -np 2 $(BUILD_DIR)/$(TARGET).o
build_and_run:
	make init && make build_main && make run