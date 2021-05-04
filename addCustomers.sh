#!/bin/bash
FILE=$1
while read LINE; do
  curl -X POST -d "$LINE" http://localhost:8080/records/
done < $FILE
