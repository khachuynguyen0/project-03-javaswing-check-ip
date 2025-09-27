#!/bin/bash

# Compile the Java application
echo "Compiling IP Checker Application..."

# Create classes directory
mkdir -p classes

# Compile Java files
javac -d classes -sourcepath src/main/java src/main/java/com/ipchecker/IPCheckerApp.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "To run the application, use: ./run.sh"
else
    echo "Compilation failed!"
    exit 1
fi