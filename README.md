# Chillguy

Chillguy is a console task manager for todos, deadlines, and events, with keyword search and automatic saving.

Read the [Chillguy User Guide](docs/README.md) for setup instructions, command examples, and troubleshooting.
The sections below cover building the project and working with its source code.

## Building and running a fat JAR

Install **JDK 25** and check that `java -version` and `javac -version` both report version 25.
If `JAVA_HOME` is set, it must point to your JDK 25 installation.
Run the following commands from the project root (the folder containing `build.gradle`).

### Create the JAR

On Windows PowerShell:

```powershell
.\gradlew.bat clean shadowJar
```

On macOS or Linux:

```sh
sh gradlew clean shadowJar
```

The included Gradle 9.7.1 wrapper downloads the pinned Gradle version automatically; a separate
Gradle installation is unnecessary. The first build needs internet access to download Gradle,
the Shadow plugin, and its dependencies. `clean` removes previous build output, and `shadowJar`
compiles the application and packages its classes, resources, and runtime dependencies into one JAR.

### Locate and run the JAR

After `BUILD SUCCESSFUL`, find the fat JAR at **`build/libs/chillguy-all.jar`**.
Run it from the project root with Java 25:

```sh
java -jar build/libs/chillguy-all.jar
```

Enter commands such as `todo read book`, `list`, and `bye` in the terminal.
You can copy this JAR to another folder or computer and run `java -jar chillguy-all.jar` there.
Java 25 must be installed on that computer; Java itself is not bundled in the JAR.
Saved tasks live in `data/chillguy.txt` relative to the directory where you launch the app.

### Build configuration

- `settings.gradle` gives the project the stable name `chillguy`.
- `build.gradle` selects the Java 25 toolchain and applies `application` and Shadow 9.6.1
  (`com.gradleup.shadow`). The application entry point is `chillguy.Chillguy`; Shadow uses this
  to set the JAR's `Main-Class` manifest entry so `java -jar` can launch it.
- The `shadowJar` task fixes the output name as `chillguy-all.jar`. The project currently has no
  external runtime dependencies; future `implementation` or `runtimeOnly` dependencies will be bundled too.
- `gradlew`, `gradlew.bat`, and `gradle/wrapper/` provide the wrapper for both platforms.
  Its distribution checksum verifies the downloaded Gradle archive.

See the [Shadow application plugin documentation](https://gradleup.com/shadow/application-plugin/)
and [Gradle Java compatibility table](https://docs.gradle.org/current/userguide/compatibility.html).

To run the recorded console tests, install Python 3 and run `python test/run-ui-tests.py`.
The runner builds the fat JAR and checks both the existing console cases and a direct JAR launch,
using disposable data folders. Results are saved in `test/ui-test-transcript.md`.

## Finding tasks

Use `find KEYWORD` to search task descriptions, for example `find book`.
Matching is case-sensitive and accepts partial words or a phrase such as `find read book`.
Dates, times, task types, and completion icons are not searched.
Results keep their stored order and completion status, with numbering starting at 1.
Use `list` to get the full-list task numbers before marking, unmarking, or deleting a task.
Searching does not change tasks or the saved file. An empty keyword shows usage guidance;
an empty list or a search with no matches shows `No matching tasks found.`

## Saving and loading tasks

Run Chillguy with Java 25 from the project root. Tasks load from `data/chillguy.txt` at startup.
Adding, deleting, marking, or unmarking a task automatically saves the list before confirming the change.
The app creates the data folder on the first save; a missing or empty file starts an empty list.
The local data folder is ignored by Git.

The file uses UTF-8 and one task per line. Status `1` means done; `0` means not done:

```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

Pipes and backslashes inside fields are escaped as `\|` and `\\`; line breaks use `\n` and `\r`.
Blank lines and an optional initial UTF-8 byte order mark are accepted.
An unreadable file or malformed record stops startup with an error; fix the file and restart.
The app leaves the original file untouched in that case.

Saving writes a temporary file and replaces the saved file atomically, so a failed write cannot leave a partial list.
If saving fails, the attempted task change is undone. Check folder permissions or close programs locking the file,
then retry the command. The file system must support atomic file replacement.
Use one running Chillguy instance per data file.

## Code organization (A-MoreOOP)

- `Chillguy` creates the collaborators, loads saved tasks, and dispatches parsed commands.
- `ui.Ui` reads console input and displays greetings, task lists, confirmations, and errors.
- `parser.Parser` validates command syntax and creates a `Command` record containing the
  operation, a new task when needed, and a task number when needed. Parsing does not change saved tasks.
- `task.TaskList` owns the ordered collection, checks task-number bounds, and performs additions,
  deletions, and status changes. It saves each change through `Storage` and undoes it if saving fails.
- `storage.Storage` retains responsibility for reading and writing the task file.

This keeps syntax, presentation, and task changes in separate classes while preserving the console
messages and storage format. `TaskList` deliberately depends on `Storage` so saving and rollback
cannot be omitted by the command dispatcher. The tradeoff is that task operations require storage;
a separate service could manage persistence if an in-memory-only task list becomes necessary.

The command record and one dispatch switch keep this increment small. Separate command subclasses
could be useful when commands develop substantially different execution logic, at the cost of more files.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. Load the Gradle project when prompted and set the **Gradle JVM** to **JDK 25** under
   `Settings` > `Build, Execution, Deployment` > `Build Tools` > `Gradle`. Use the Gradle wrapper.
1. After that, locate the `src/main/java/chillguy/Chillguy.java` file, right-click it, and choose `Run Chillguy.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
