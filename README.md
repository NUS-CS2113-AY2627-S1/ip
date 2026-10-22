# Dawn

Dawn is a desktop Command Line Interface (CLI) task management chatbot tailored to help you keep track of your daily tasks, deadlines, and events efficiently. Named after the Pokémon trainer and featuring a friendly Piplup personality, Dawn combines keyboard-driven speed with reliable, transparent task tracking.

For the comprehensive user documentation, check out the [Dawn User Guide](docs/README.md).

---

## Features

* **Task Tracking:** Easily add and organize `todo`, `deadline`, and `event` tasks.
* **Flexible Date & Time Formats:** Recognizes multiple date patterns including `yyyy-MM-dd`, `yyyy/MM/dd`, `d/M/yyyy`, `dd-MM-yyyy`, and `d MMM yyyy` (e.g. `12 Oct 2026`) with optional 24-hour `HHmm` times.
* **Schedule & Search:** Filter tasks occurring on a specific calendar date with `view [date]`, or search descriptions by keyword using `find [keyword]`.
* **Task Management:** Mark tasks as done (`mark`), unmark them (`unmark`), or permanently delete them (`delete`) by index.
* **On-Demand Help:** Display the full command syntax table anytime with `help`.
* **Resilient Data Storage:** Automatically saves tasks to `data/dawn.txt` in a versioned format (`V2`) with background recovery that gracefully skips corrupted records without losing intact tasks.

---

## Quick Start

### Prerequisites
* **Java 25** or above installed on your computer.

### Running the Application
1. Download the latest [`dawn.jar`](https://github.com/naieel04/ip/releases) release.
2. Open a command prompt or terminal in the folder containing `dawn.jar`.
3. Launch the application:
   ```bash
   java -jar dawn.jar
   ```
4. You will be greeted by Piplup's welcome banner and the command guide. Start typing commands to manage your tasks!

---

## Setting up in IntelliJ IDEA

Prerequisites: JDK 25, update IntelliJ IDEA to the most recent version.

1. Open IntelliJ IDEA (if you are not in the welcome screen, click `File` > `Close Project` to close any existing project first).
2. Open the project into IntelliJ as follows:
   1. Click `Open`.
   2. Select the project directory, and click `OK`.
   3. If there are any further prompts, accept the defaults.
3. Configure the project to use **JDK 25** (not other versions) as explained [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
4. Locate the `src/main/java/dawn/Dawn.java` file, right-click it, and choose `Run 'Dawn.main()'`. If the setup is correct, you should see something like the following in the console:
   ```text
   ____________________________________________________________

   [Piplup ASCII Art Banner]

   Piplup here! Ready when you are!

   Here are the commands you can use:
   +---------------------------------------------+-----------------------+
   | Command / format                            | Description           |
   +---------------------------------------------+-----------------------+
   | todo [description]                          | Add a todo            |
   | deadline [description] /by [due date]       | Add a deadline        |
   | event [description] /from [start] /to [end] | Add an event          |
   | list                                        | List all tasks        |
   | mark [task number]                          | Mark a task done      |
   | unmark [task number]                        | Mark a task not done  |
   | delete [task number]                        | Delete a task         |
   | view [date]                                 | View tasks on a date  |
   | find [keyword]                              | Find tasks by keyword |
   | help                                        | Show commands         |
   | bye                                         | Exit Dawn             |
   +---------------------------------------------+-----------------------+
   ____________________________________________________________
   ```

**Warning:** Keep the `src/main/java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location build tools (e.g., Gradle) expect to find Java files.

---

## Building a Runnable JAR

The project uses the Gradle Shadow plugin to build a standalone fat JAR containing the application and all dependencies.

From the repository root, run:

* **Windows (PowerShell):**
  ```powershell
  .\gradlew.bat shadowJar
  ```
* **macOS / Linux:**
  ```bash
  ./gradlew shadowJar
  ```

The generated executable JAR will be located at `build/libs/dawn.jar`. You can run it with Java 25:

```bash
java -jar build/libs/dawn.jar
```

---

## Documentation

* [User Guide](docs/README.md): Detailed explanations, command syntax, usage examples, expected outputs, and FAQs.
