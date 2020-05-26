#!/bin/bash

NOW=$(date +"%Y-%m-%d %T")

CPU=$(top -bn1 | grep load | awk '{printf "%.2f%%", $(NF-2)}')

MEM=$(free -m | awk 'NR==2{printf "%.2f%%", $3*100/$2 }')

DFR=$(df -h | awk '$NF=="/"{printf "%s", $5}')

echo "$NOW;$CPU;$MEM;$DFR"
