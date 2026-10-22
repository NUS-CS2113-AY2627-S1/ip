#!/usr/bin/env bash
# Navigate to the Java source folder and compile
cd ../src/main/java
javac *.java

# Run Yoda with input.txt as stdin, capture output to ACTUAL.txt
java Yoda < ../../../text-ui-test/input.txt > ../../../text-ui-test/ACTUAL.txt

# Compare ACTUAL.txt to EXPECTED.txt and report differences
diff ../../../text-ui-test/ACTUAL.txt ../../../text-ui-test/EXPECTED.txt
if [ $? -eq 0 ]; then
    echo "All tests passed!"
else
    echo "Test FAILED. See differences above."
fi