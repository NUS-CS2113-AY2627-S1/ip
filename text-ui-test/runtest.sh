#!/usr/bin/env bash

cd "$(dirname "$0")/.." || exit 1
test_bin="_temp/text-ui-test-bin"
test_work="_temp/text-ui-test-work"
mkdir -p _temp
rm -rf "$test_bin"
rm -rf "$test_work"
mkdir -p "$test_bin"
mkdir -p "$test_work"

if ! javac -cp src/main/java -Xlint:all -d "$test_bin" src/main/java/bob/*.java; then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

cd "$test_work" || exit 1

java -classpath ../text-ui-test-bin bob.Bob < ../../text-ui-test/input.txt > ACTUAL.TXT
if ! diff --strip-trailing-cr ACTUAL.TXT ../../text-ui-test/EXPECTED.TXT; then
    echo "********** UI TEST FAILURE **********"
    exit 1
fi

if ! diff --strip-trailing-cr data/bob.txt ../../text-ui-test/EXPECTED-DATA.TXT; then
    echo "********** STORAGE TEST FAILURE **********"
    exit 1
fi

java -classpath ../text-ui-test-bin bob.Bob < ../../text-ui-test/input-load.txt > ACTUAL-LOAD.TXT
if ! diff --strip-trailing-cr ACTUAL-LOAD.TXT ../../text-ui-test/EXPECTED-LOAD.TXT; then
    echo "********** STORAGE LOAD TEST FAILURE **********"
    exit 1
fi

cp ../../text-ui-test/CORRUPTED-DATA.TXT data/bob.txt
java -classpath ../text-ui-test-bin bob.Bob < ../../text-ui-test/input-list.txt > ACTUAL-CORRUPT.TXT
if ! diff --strip-trailing-cr ACTUAL-CORRUPT.TXT ../../text-ui-test/EXPECTED-CORRUPT.TXT; then
    echo "********** CORRUPTED DATA TEST FAILURE **********"
    exit 1
fi

rm data/bob.txt
mkdir data/bob.txt
java -classpath ../text-ui-test-bin bob.Bob < ../../text-ui-test/input-error.txt > ACTUAL-IO-ERROR.TXT
if ! diff --strip-trailing-cr ACTUAL-IO-ERROR.TXT ../../text-ui-test/EXPECTED-IO-ERROR.TXT; then
    rmdir data/bob.txt
    echo "********** DATA FILE ERROR TEST FAILURE **********"
    exit 1
fi
rmdir data/bob.txt

java -classpath ../text-ui-test-bin bob.Bob < /dev/null > ACTUAL-EMPTY-INPUT.TXT
if ! diff --strip-trailing-cr ACTUAL-EMPTY-INPUT.TXT ../../text-ui-test/EXPECTED-EMPTY-INPUT.TXT; then
    echo "********** EMPTY INPUT TEST FAILURE **********"
    exit 1
fi

java -classpath ../text-ui-test-bin bob.Bob < ../../text-ui-test/input-deadline.txt > ACTUAL-DEADLINE.TXT
if ! grep -Fq "[D][ ] return book (by: 31/12/2099 1800)" ACTUAL-DEADLINE.TXT; then
    echo "********** DEADLINE OUTPUT TEST FAILURE **********"
    exit 1
fi
if ! grep -Fq "The clock's tickin'! You've got " ACTUAL-DEADLINE.TXT; then
    echo "********** DEADLINE COUNTDOWN TEST FAILURE **********"
    exit 1
fi
if ! diff --strip-trailing-cr data/bob.txt ../../text-ui-test/EXPECTED-DEADLINE-DATA.TXT; then
    echo "********** DEADLINE STORAGE TEST FAILURE **********"
    exit 1
fi

echo "Test result: PASSED"
cd ../.. || exit 1
rm -rf "$test_bin"
rm -rf "$test_work"
