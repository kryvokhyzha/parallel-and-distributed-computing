cmake_minimum_required(VERSION 3.17)
project(Assignment6)

set(CMAKE_CXX_STANDARD 14)
include_directories(/usr/local/openmpi/include/mpi/)

set(CMAKE_C_COMPILER "/usr/local/openmpi/bin/mpicc")
set(CMAKE_CXX_COMPILER "/usr/local/openmpi/bin/mpic++")

add_executable(Assignment6 src/main.cpp)