#!/usr/bin/env bash

# Wipe the previous build so that stale .class files from an earlier package
# layout can never be picked up instead of the freshly compiled ones.
rm -rf ../bin
mkdir ../bin

# delete output and saved data from the previous run
rm -f ACTUAL.TXT ACTUAL_SAVE.TXT ACTUAL_RELOAD.TXT
rm -rf data

# compile the code into the bin folder, terminates if error occurred
# find is used instead of a glob because the sources live in nested packages
if ! javac -cp ../src/main/java -Xlint:none -d ../bin $(find ../src/main/java -name "*.java")
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt and redirect the output to ACTUAL.TXT
java -classpath ../bin lumi.Lumi < input.txt > ACTUAL.TXT

# the console output tells us nothing about saving, so collect the save file too
if [ -f data/lumi.txt ]
then
    cp data/lumi.txt ACTUAL_SAVE.TXT
else
    echo "********** NO SAVE FILE WAS WRITTEN **********"
    exit 1
fi

# start a second session so that loading the saved file is exercised too
java -classpath ../bin lumi.Lumi < input_reload.txt > ACTUAL_RELOAD.TXT

# convert to UNIX format before comparing
cp EXPECTED.TXT EXPECTED-UNIX.TXT
cp EXPECTED_SAVE.TXT EXPECTED_SAVE-UNIX.TXT
cp EXPECTED_RELOAD.TXT EXPECTED_RELOAD-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT ACTUAL_SAVE.TXT EXPECTED_SAVE-UNIX.TXT     ACTUAL_RELOAD.TXT EXPECTED_RELOAD-UNIX.TXT 2>/dev/null

status=0

echo "--- console output ---"
if diff ACTUAL.TXT EXPECTED-UNIX.TXT
then
    echo "console: PASSED"
else
    echo "console: FAILED"
    status=1
fi

echo "--- saved data ---"
if diff ACTUAL_SAVE.TXT EXPECTED_SAVE-UNIX.TXT
then
    echo "saved data: PASSED"
else
    echo "saved data: FAILED"
    status=1
fi

echo "--- reloaded session ---"
if diff ACTUAL_RELOAD.TXT EXPECTED_RELOAD-UNIX.TXT
then
    echo "reloaded session: PASSED"
else
    echo "reloaded session: FAILED"
    status=1
fi

if [ $status -eq 0 ]
then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
fi
exit $status
