# Turing

A task-tracking chatbot that lives in your terminal. Tell it what you have to
do — todos, deadlines, events — and it keeps the list for you, saving it
automatically so nothing is lost between runs.

📖 **[User Guide](https://ranoot.github.io/ip/)** — how to use Turing.

```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Running it

Download `turing.jar` from the [latest release](https://github.com/ranoot/ip/releases),
then, with JDK 25 or later on your `PATH`:

```
java -jar turing.jar
```

**Note:** the chatbot saves your tasks to `data/turing.txt` *relative to the
folder you run it from*, not to wherever the JAR sits. Run it from the folder
you want that data in.

---

The rest of this file is for working on Turing rather than using it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/turing/Turing.java` file, right-click it, and choose `Run Turing.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    _____ _   _ ____  ___ _   _  ____ 
   |_   _| | | |  _ \|_ _| \ | |/ ___|
     | | | | | | |_) || ||  \| | |  _ 
     | | | |_| |  _ < | || |\  | |_| |
     |_|  \___/|_| \_\___|_| \_|\____|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Building an executable JAR

Prerequisite: JDK 25 on your `PATH`. Gradle itself does not have to be
installed: the wrapper script fetches the right version the first time it runs.

From the project root:

```
./gradlew clean shadowJar
```

or, on Windows, `gradlew.bat clean shadowJar`.

This compiles everything under `src/main/java` and packages it, together with
anything it depends on, into `build/libs/turing.jar`. `turing.Turing` is
recorded in the JAR's manifest as the class to start, so the chatbot runs with:

```
java -jar build/libs/turing.jar
```

The JAR is self-contained, so it can be copied anywhere and run on any machine
with a JDK 25 runtime.

To run the chatbot from the sources without packaging it first:

```
./gradlew run
```

`build/` and `data/` are both git-ignored, being a build product and the user's
own data rather than source.

## Documentation

The user guide lives in [`docs/README.md`](docs/README.md) and is published
through GitHub Pages from the `/docs` folder of `master`.
