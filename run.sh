#!/bin/bash

# Run the IP Checker Application
echo "Starting IP Checker Application..."

# Check if classes exist
if [ ! -d "classes" ]; then
    echo "Classes not found. Running build first..."
    ./build.sh
fi

# Run the application
java -cp classes com.ipchecker.IPCheckerApp