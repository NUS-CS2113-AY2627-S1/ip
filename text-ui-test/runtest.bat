@ECHO OFF

REM wipe the previous build so stale .class files cannot be picked up
if exist ..\bin rmdir /s /q ..\bin
mkdir ..\bin

REM delete output and saved data from the previous run
if exist ACTUAL.TXT del ACTUAL.TXT
if exist ACTUAL_SAVE.TXT del ACTUAL_SAVE.TXT
if exist ACTUAL_RELOAD.TXT del ACTUAL_RELOAD.TXT
if exist data rmdir /s /q data

REM compile the code into the bin folder, terminates if error occurred
REM sources are listed to a file because they live in nested packages
dir /s /B ..\src\main\java\*.java > sources.txt
javac -cp ..\src\main\java -Xlint:none -d ..\bin @sources.txt
IF ERRORLEVEL 1 (
    del sources.txt
    echo ********** BUILD FAILURE **********
    exit /b 1
)
del sources.txt

REM run the program, feed commands from input.txt and redirect the output to ACTUAL.TXT
java -classpath ..\bin lumi.Lumi < input.txt > ACTUAL.TXT

REM the console output tells us nothing about saving, so collect the save file too
if not exist data\lumi.txt (
    echo ********** NO SAVE FILE WAS WRITTEN **********
    exit /b 1
)
copy /Y data\lumi.txt ACTUAL_SAVE.TXT > nul

REM start a second session so that loading the saved file is exercised too
java -classpath ..in lumi.Lumi < input_reload.txt > ACTUAL_RELOAD.TXT

echo --- console output ---
FC ACTUAL.TXT EXPECTED.TXT
echo --- saved data ---
FC ACTUAL_SAVE.TXT EXPECTED_SAVE.TXT
echo --- reloaded session ---
FC ACTUAL_RELOAD.TXT EXPECTED_RELOAD.TXT
