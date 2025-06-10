#!/bin/bash

# Initialize variables
input_file=""

# Parse command line options
while getopts "i:" opt; do
    case $opt in
        i) input_file="$OPTARG";;
        *) echo "Usage: $0 -i <filename>" >&2
           exit 1;;
    esac
done

# Check if input file was provided
if [ -z "$input_file" ]; then
    echo "Error: Input file not provided" >&2
    echo "Usage: $0 -i <filename>" >&2
    exit 1
fi

# Check if the file exists
if [ ! -f "$input_file" ]; then
    echo "File '$input_file' does not exist" >&2
    exit 1
fi

# Get and output only the file size in bytes
wc -c < "$input_file"
