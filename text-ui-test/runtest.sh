#!/usr/bin/env bash
set -euo pipefail

rm -rf ../bin
mkdir ../bin
rm -f ACTUAL.txt

find .. -name "*.java" > sources.txt
if ! javac -Xlint:none -d ../bin @sources.txt; then
    echo "********** BUILD FAILURE **********"
    exit 1
fi
rm sources.txt

java -cp ../bin dook/Dook 412 . < input.txt > ACTUAL.txt

if diff -u ACTUAL.txt EXPECTED.txt; then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
    exit 1
fi
