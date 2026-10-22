# Ace

Ace is a command-line task manager for tracking to-dos, deadlines, and events.
It supports marking tasks as completed, deleting tasks, searching descriptions,
and saving your tasks between sessions.

See the [Ace User Guide](https://github.com/camimaigrot/ip/blob/master/docs/README.md) for command syntax, examples, and screenshots.

## Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA if running Ace from the source code in an IDE

## Run in IntelliJ IDEA

1. Clone or download this repository and open the project in IntelliJ IDEA.
2. In **File → Project Structure → Project**, set **Project SDK** to **JDK 25**
   and **Language level** to **SDK default**.
3. Open `src/main/java/ace/Ace.java`.
4. Right-click `Ace.java` and select **Run 'Ace.main()'**.
5. Enter commands in the Run console, or type `help` for instructions.

For example:

```text
todo read book
deadline submit report /by 2026-10-15
list
find book
bye
```

Ace stores tasks in `data/ace.txt`, which is created when necessary.

## Build and run from the terminal

On a system with Make and JDK 25 installed, use:

```sh
make run
```

Run the automated regression tests with:

```sh
make test
```

## Run the released application

1. Install Java 25.
2. Download `ip.jar` from the [latest release](https://github.com/camimaigrot/ip/releases/latest).
3. Open a terminal in the folder containing the JAR.
4. Run:

   java -jar ip.jar
