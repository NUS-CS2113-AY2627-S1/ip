# JAR Distribution Verification

Result: PASS

Setup: Created an empty temporary folder, copied only chillguy-all.jar into it, and launched Java with that folder as the working directory. No source files, Gradle files, classpath, or pre-existing data were provided.

Build: `.\gradlew.bat --offline --console=plain shadowJar` — BUILD SUCCESSFUL; current classes and JAR up to date.

Java:

```text
openjdk version "25.0.4" 2026-07-21 LTS
OpenJDK Runtime Environment Temurin-25.0.4+7 (build 25.0.4+7-LTS)
OpenJDK 64-Bit Server VM Temurin-25.0.4+7 (build 25.0.4+7-LTS, mixed mode, sharing)
```

Manifest:

```text
Manifest-Version: 1.0
Main-Class: chillguy.Chillguy

```

Packaged application classes: 7.

Command: `java -jar "chillguy-all.jar"`

Input:

```text
todo read book
list
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

Saved data/chillguy.txt:

```text
T | 0 | read book
```

Expected output and saved data: the Run the Fat JAR case in test/ui-test-plan.md.
