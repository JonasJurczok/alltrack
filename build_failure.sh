#!/usr/bin/env sh
echo "Current directory is $(pwd)"
echo "\n=== TEST FAILURE REPORTS ===\n"

for F in app/build/reports/androidTests/connected/*.html
do
    echo $F
    cat $F
    echo
done