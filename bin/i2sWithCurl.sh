#!/bin/bash

# Default values
DEFAULT_SERVER="localhost"
DEFAULT_PORT="9141"

# Check if image file is provided
if [ $# -lt 1 ]; then
    echo "Executes osr file processing on running server, curl and jq are required"
    echo "Usage: $0 IMAGE_FILE [SERVER [PORT]]"
    echo "Example: $0 image.png localhost 9141"
    exit 1
fi

# Parse arguments
IMAGE_FILE="$1"
SERVER="${2:-$DEFAULT_SERVER}"
PORT="${3:-$DEFAULT_PORT}"

# Check if image file exists
if [ ! -f "$IMAGE_FILE" ]; then
    echo "Error: Image file '$IMAGE_FILE' does not exist"
    exit 1
fi

# Check if curl is available
if ! command -v curl &> /dev/null; then
    echo "Error: curl is not installed"
    exit 1
fi

# Make the API call
echo "Processing image '$IMAGE_FILE' on server $SERVER:$PORT..."

# Store the response and status code
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -d "{\"imageFilename\":\"$IMAGE_FILE\"}" \
    "http://$SERVER:$PORT/img2struct/performOSR")

# Extract status code and response body
HTTP_STATUS=$(echo "$RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

# Check curl exit status
if [ $? -ne 0 ]; then
    echo "Error: Failed to connect to server at $SERVER:$PORT"
    exit 1
fi

# Check HTTP status code
if [ "$HTTP_STATUS" -ne 200 ]; then
    echo "Error: Server returned status code $HTTP_STATUS"
    echo "Server response:"
    echo "$RESPONSE_BODY" | jq -r '.'
    exit 1
fi

# If successful, print the OSR result
echo "$RESPONSE_BODY" | jq -r '.osrResult.rawData' 