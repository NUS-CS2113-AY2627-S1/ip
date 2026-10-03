# Clammy

Clammy is a command-line task manager written in Java. It keeps track of todo items, deadlines, and events.
Tasks can be listed, marked as completed or incomplete, and deleted. Changes are saved automatically and
loaded the next time Clammy starts.

Read the [published user guide](https://j-unn-y.github.io/ip/) for setup instructions and command examples,
or download the application from the [latest release](https://github.com/J-unn-y/ip/releases/latest).
The [user guide source](docs/README.md) is also available in this repository.

## Requirements

- JDK 25
- A terminal or IntelliJ IDEA

The included Gradle wrapper downloads Gradle 9.2.1 when first used, so you do not need to install Gradle
separately. The first build requires internet access to download Gradle and the Shadow plugin.

On macOS with SDKMAN, select the project Java version when necessary:

```bash
sdk use java 25.0.3.fx-zulu
```

Confirm that the correct compiler is active:

```bash
javac -version
```

The result should report Java 25.

## Run a downloaded release

1. Install Java 25 and check it with `java -version`.
2. Download `clammy-all.jar` from this fork's [GitHub releases](https://github.com/J-unn-y/ip/releases).
3. Copy the JAR into an empty folder and open a terminal in that folder.
4. Run:

   ```bash
   java -jar "clammy-all.jar"
   ```

Clammy creates `data/clammy.txt` beside the JAR when you first change a task. Run it from the same folder
next time to load your saved tasks. Source files and Gradle are not needed to run the downloaded JAR.
If you rename the JAR, use the new filename in the command and keep the double quotes for names with
spaces or brackets. Running from a terminal is the supported workflow; double-clicking is not required.

## Build and run a fat JAR

Run these commands from the project root:

```bash
./gradlew shadowJar
java -jar "build/libs/clammy-all.jar"
```

`shadowJar` compiles the source files and packages the application and its runtime dependencies into
**`build/libs/clammy-all.jar`**. This is the fat JAR to distribute. The filename is configured explicitly in
`build.gradle`, so it stays the same when the project folder is renamed. Generated files under `build/` are
ignored by Git.

Do not commit this generated application JAR. Attach it as a binary asset to a GitHub release instead.
The small `gradle/wrapper/gradle-wrapper.jar` belongs in Git: it is the build launcher, not the generated
Clammy application. Release notes for the first release are in [docs/release-notes-v0.1.md](docs/release-notes-v0.1.md).

The build uses the [Shadow plugin](https://gradleup.com/shadow/) (`com.gradleup.shadow`, version 9.6.1)
with Gradle's `application` plugin. Setting `mainClass` to `clammy.Clammy` adds the entry point to the JAR
manifest, allowing `java -jar` to start Clammy. The Java toolchain is set to Java 25.

Clammy currently has no third-party runtime dependencies, so the JAR is small. Shadow will also bundle
runtime dependencies added to the build later. A fat JAR does not include Java itself: the machine running
it needs Java 25. You can share the JAR without sharing the source code or installing Gradle on that machine.

Run the JAR from the project root to continue using the project's `data/clammy.txt`. If you copy the JAR
elsewhere, saved data is relative to the terminal's working directory, not the JAR's location. Clammy creates
the data folder on its first save. See [saved tasks](docs/storage.md) for format and error handling.

Run `./gradlew shadowJar` again after changing the source code. To remove previous build outputs first, use:

```bash
./gradlew clean shadowJar
```

If the shell reports `permission denied` when launching the wrapper, run `chmod +x gradlew` once.

## Run from source with Gradle

```bash
./gradlew --console=plain run
```

Gradle compiles all Java packages automatically and forwards terminal input to Clammy.

## Commands

### Add a todo

```text
todo DESCRIPTION
```

Example:

```text
todo borrow book
```

### Add a deadline

```text
deadline DESCRIPTION /by DATE_OR_TIME
```

Example:

```text
deadline return book /by 2/12/2019 1800
```

This displays `return book (by: Dec 02 2019, 6:00 PM)`. Deadlines accept `yyyy-MM-dd`
or day-first `d/M/yyyy`, optionally followed by a 24-hour time in `HHmm` format.
For example, `deadline submit report /by 2019-10-15` displays the date as `Oct 15 2019`.
Impossible dates and times are rejected, including `2019-02-29` and `2400`.
Date-only deadlines use midnight internally but display only the date; an explicit
`0000` displays `12:00 AM`. Deadline values use Java's `LocalDateTime` without a time zone.
Event start/end fields still accept free-form text.

### Add an event

```text
event DESCRIPTION /from START /to END
```

Example:

```text
event project meeting /from Monday 2pm /to 4pm
```

### List tasks

```text
list
```

### Find tasks by description

```text
find book
```

Displays tasks whose descriptions contain `book`, ignoring case (including `Book` and `notebook`).
You can also search for a phrase, such as `find return book`; spaces within the phrase are matched literally.
The keyword must not be blank. Dates, event times, and completion markers are not searched.
Matches retain their original order and completion status. An empty result displays only
`Here are the matching tasks in your list:`. Searching does not change or save tasks.

Search results are numbered from 1 for display. Use `list` to get the full-list task numbers
before using `mark`, `unmark`, or `delete`.

### Mark a task as completed

Task numbers are shown by the `list` command.

```text
mark TASK_NUMBER
```

Example:

```text
mark 1
```

### Mark a task as incomplete

```text
unmark TASK_NUMBER
```

Example:

```text
unmark 1
```

### Delete a task

Use a task number from `list` to remove that task:

```text
delete TASK_NUMBER
```

For example, `delete 2` removes the second task in the full list. Deletion is saved automatically
and cannot be undone. Run `list` again after deleting because later task numbers shift down.

### Exit Clammy

```text
bye
```

## Run in IntelliJ IDEA

1. Open this project directory in IntelliJ IDEA.
2. Set the Project SDK to JDK 25 and the language level to `SDK default`.
3. Open `src/main/java/clammy/Clammy.java`.
4. Run `Clammy.main()` using the run icon beside the `main` method.

Keep Java source files under `src/main/java`. This is the standard source location expected by Java build tools.

## UI test plan

The command-line test cases and their expected output are recorded in
[`test/ui-test-plan.md`](test/ui-test-plan.md).
