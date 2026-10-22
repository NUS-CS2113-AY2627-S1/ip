@ECHO OFF
SETLOCAL

PUSHD %~dp0\..

SET TEST_BIN=_temp\text-ui-test-bin
SET TEST_WORK=_temp\text-ui-test-work
IF NOT EXIST _temp MKDIR _temp
IF EXIST "%TEST_BIN%" RMDIR /S /Q "%TEST_BIN%"
IF EXIST "%TEST_WORK%" RMDIR /S /Q "%TEST_WORK%"
MKDIR "%TEST_BIN%"
MKDIR "%TEST_WORK%"

javac -cp src\main\java -Xlint:all -d "%TEST_BIN%" src\main\java\bob\*.java
IF ERRORLEVEL 1 (
    ECHO ********** BUILD FAILURE **********
    POPD
    EXIT /B 1
)

PUSHD "%TEST_WORK%"

java -classpath ..\text-ui-test-bin bob.Bob < ..\..\text-ui-test\input.txt > ACTUAL.TXT
FC ACTUAL.TXT ..\..\text-ui-test\EXPECTED.TXT
IF ERRORLEVEL 1 (
    ECHO ********** UI TEST FAILURE **********
    POPD
    EXIT /B 1
)

FC data\bob.txt ..\..\text-ui-test\EXPECTED-DATA.TXT
IF ERRORLEVEL 1 (
    ECHO ********** STORAGE TEST FAILURE **********
    POPD
    EXIT /B 1
)

java -classpath ..\text-ui-test-bin bob.Bob < ..\..\text-ui-test\input-load.txt > ACTUAL-LOAD.TXT
FC ACTUAL-LOAD.TXT ..\..\text-ui-test\EXPECTED-LOAD.TXT
IF ERRORLEVEL 1 (
    ECHO ********** STORAGE LOAD TEST FAILURE **********
    POPD
    EXIT /B 1
)

COPY /Y ..\..\text-ui-test\CORRUPTED-DATA.TXT data\bob.txt > NUL
java -classpath ..\text-ui-test-bin bob.Bob < ..\..\text-ui-test\input-list.txt > ACTUAL-CORRUPT.TXT
FC ACTUAL-CORRUPT.TXT ..\..\text-ui-test\EXPECTED-CORRUPT.TXT
IF ERRORLEVEL 1 (
    ECHO ********** CORRUPTED DATA TEST FAILURE **********
    POPD
    EXIT /B 1
)

DEL data\bob.txt
MKDIR data\bob.txt
java -classpath ..\text-ui-test-bin bob.Bob < ..\..\text-ui-test\input-error.txt > ACTUAL-IO-ERROR.TXT
FC ACTUAL-IO-ERROR.TXT ..\..\text-ui-test\EXPECTED-IO-ERROR.TXT
IF ERRORLEVEL 1 (
    RMDIR data\bob.txt
    ECHO ********** DATA FILE ERROR TEST FAILURE **********
    POPD
    EXIT /B 1
)
RMDIR data\bob.txt

java -classpath ..\text-ui-test-bin bob.Bob < NUL > ACTUAL-EMPTY-INPUT.TXT
FC ACTUAL-EMPTY-INPUT.TXT ..\..\text-ui-test\EXPECTED-EMPTY-INPUT.TXT
IF ERRORLEVEL 1 (
    ECHO ********** EMPTY INPUT TEST FAILURE **********
    POPD
    EXIT /B 1
)

java -classpath ..\text-ui-test-bin bob.Bob < ..\..\text-ui-test\input-deadline.txt > ACTUAL-DEADLINE.TXT
FINDSTR /L /C:"[D][ ] return book (by: 31/12/2099 1800)" ACTUAL-DEADLINE.TXT > NUL
IF ERRORLEVEL 1 (
    ECHO ********** DEADLINE OUTPUT TEST FAILURE **********
    POPD
    EXIT /B 1
)
FINDSTR /L /C:"The clock's tickin'! You've got " ACTUAL-DEADLINE.TXT > NUL
IF ERRORLEVEL 1 (
    ECHO ********** DEADLINE COUNTDOWN TEST FAILURE **********
    POPD
    EXIT /B 1
)
FC data\bob.txt ..\..\text-ui-test\EXPECTED-DEADLINE-DATA.TXT
IF ERRORLEVEL 1 (
    ECHO ********** DEADLINE STORAGE TEST FAILURE **********
    POPD
    EXIT /B 1
)

ECHO Test result: PASSED
POPD
RMDIR /S /Q "%TEST_BIN%"
RMDIR /S /Q "%TEST_WORK%"
POPD
EXIT /B 0
