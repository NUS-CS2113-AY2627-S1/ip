# Yanny project template

This is a project template for a greenfield Java project. It's named after the creator  of the bot Yan Hern and is appropriately named Yanny_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/yanny/ui/Yanny.java` file, right-click it, and choose `Run 'Yanny.main()'` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
```text
____    ____  ___      .__   __. .__   __. ____    ____
\   \  /   / /   \     |  \ |  | |  \ |  | \   \  /   /
 \   \/   / /  ^  \    |   \|  | |   \|  |  \   \/   /
  \_    _/ /  /_\  \   |  . `  | |  . `  |   \_    _/
    |  |  /  _____  \  |  |\   | |  |\   |     |  |
    |__| /__/     \__\ |__| \__| |__| \__|     |__|
```

**Warning:** Keep `src\main\java` as the source root and place Java files in directories matching their package names, as this is the default location expected by Java tools.

## Command quick reference

Enter these commands after starting Yanny. Replace values in angle brackets with
your own task details.

| Command | Purpose | Example |
| --- | --- | --- |
| `todo <description>` | Add a task without a date or time. | `todo borrow a book` |
| `deadline <description> /by <yyyy-MM-dd or d/M/yyyy HHmm>` | Add a task due on a date or at a specified time. | `deadline submit report /by 2/12/2019 1800` |
| `event <description> /from <start> /to <end>` | Add an event with a start and end. | `event project meeting /from Monday 2pm /to Monday 3pm` |
| `list` | Display all tasks and their numbers. | `list` |
| `find <keyword>` | Find tasks by text in their descriptions. | `find book` |
| `mark <number>` | Mark a task as completed. | `mark 2` |
| `unmark <number>` | Mark a task as not completed. | `unmark 2` |
| `delete <number>` or `remove <number>` | Delete a task. | `delete 2` |
| `bye` | Exit Yanny. | `bye` |

Use the task numbers displayed by `list` or `find` with `mark`, `unmark`, `delete`,
and `remove`. See the [Yanny User Guide](docs/README.md) for detailed command usage,
examples, and storage information.

## Building and running the executable JAR

Yanny can be packaged as a self-contained executable (fat) JAR with Gradle's
Shadow plugin. Java 25 is required.

On macOS/Linux, run this from the project root:

```bash
./gradlew clean shadowJar
```

On Windows, run:

```bat
gradlew.bat clean shadowJar
```

The generated JAR is written to `build/libs/yanny.jar`. The `build` directory
and generated JAR are build artifacts and should not be committed to Git.

To run the packaged application, copy `yanny.jar` into an empty directory,
open a terminal in that directory, and run:

```bash
java -jar "yanny.jar"
```

Yanny stores tasks in `./data/yanny.txt`, relative to the directory from which
the JAR is launched. The `data` directory and file are created automatically
when the first task is saved.

For distribution, attach `build/libs/yanny.jar` to a GitHub release rather than
committing the generated binary. Create a release from the merged `master`
branch, choose a version tag such as `v0.1`, attach the JAR under **Attach
binaries**, and publish the release.
