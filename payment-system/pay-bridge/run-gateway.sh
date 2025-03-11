#!/bin/bash

# Compile the Java file
echo "Compiling SimplePaymentGateway.java"
javac SimplePaymentGateway.java

if [ $? -ne 0 ]; then
    echo "Failed to compile SimplePaymentGateway.java"
    exit 1
fi

# Run the gateway
echo "Starting SimplePaymentGateway"
java SimplePaymentGateway