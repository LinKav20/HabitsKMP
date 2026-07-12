#!/bin/bash
# Extract applicationId
APP_ID=$(grep -o 'applicationId = "[^"]*"' "$GRADLE_FILE" | sed 's/applicationId = "//' | sed 's/"//')

# Extract versionName
VERSION=$(grep -o 'versionName = "[^"]*"' "$GRADLE_FILE" | sed 's/versionName = "//' | sed 's/"//')

# Extract versionCode
VERSION_CODE=$(grep -o 'versionCode = [0-9]*' "$GRADLE_FILE" | sed 's/versionCode = //')
