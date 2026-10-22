@echo off
cd ..\main\java
javac *.java
java Yoda < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.txt
fc ..\..\text-ui-test\ACTUAL.txt ..\..\text-ui-test\EXPECTED.txt