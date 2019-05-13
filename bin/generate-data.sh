#!/usr/bin/env bash

set -euo pipefail

PARENT_PATH=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

# Create the directories
mkdir -p /tmp/spark-streaming-demo/test-data/in/
mkdir -p /tmp/spark-streaming-demo/test-data/out/

# Clean up the old data from previous runs
rm -rf /tmp/spark-streaming-demo/test-data/in/*
rm -rf /tmp/spark-streaming-demo/test-data/out/*

while true
do
    UUID=$(python -c 'import sys,uuid; sys.stdout.write(uuid.uuid4().hex)' | pbcopy && pbpaste && echo)
    cd "$PARENT_PATH"
    $(python3 data_gen.py > /tmp/spark-streaming-demo/test-data/in/"$UUID")
    # Do this in case you accidentally pass an argument
    # that finishes too quickly.
    sleep 1
done