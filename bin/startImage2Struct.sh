#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
cd "$SCRIPT_DIR/.." || exit
APP_WORKING_DIR=$(pwd)
LOG_DIR="$APP_WORKING_DIR/log"
PID_DIR="$APP_WORKING_DIR/run"
PID_FILE="$PID_DIR/image2struct.pid"

# Create necessary directories if they don't exist
mkdir -p "$PID_DIR"
mkdir -p "$LOG_DIR"

# Check if the application is already running
if [ -f "$PID_FILE" ]; then
    pid=$(cat "$PID_FILE")
    if ps -p "$pid" > /dev/null 2>&1; then
        echo "Application is already running with PID: $pid"
        exit 1
    else
        echo "Removing stale PID file"
        rm "$PID_FILE"
    fi
fi

# Start the application
echo "Starting Image2Structure application..."
java -jar "$SCRIPT_DIR/i2s.jar" > /dev/null 2>&1 &

# Save the PID
echo $! > "$PID_FILE"
echo "Application started with PID: $(cat "$PID_FILE")"
echo "Logs are being written to $LOG_DIR/"