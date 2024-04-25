#!/usr/bin/env bash

command="mvn test -Dbase.url=$BASE_URL"

echo "Run: " $command
eval $command
echo "End of running: " $command