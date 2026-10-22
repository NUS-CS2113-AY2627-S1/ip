# Vani

Vani is a command-line task manager for to-dos, deadlines, and events.
See the [user guide](docs/README.md) for commands and examples.

## Requirements

Use **JDK 25** to build the project and **Java 25** to run the JAR.
The Gradle build is configured to compile the application for Java 25.

## Building a runnable JAR

Open a terminal in the project root and confirm the Java version:

```text
java -version
```

The output should show version 25. Then build the JAR using the Gradle Wrapper.

On Windows PowerShell:

```powershell
.\gradlew.bat clean shadowJar
```

On macOS or Linux:

```sh
./gradlew clean shadowJar
```

If macOS or Linux reports a permission error for `gradlew`, run `chmod +x gradlew` once and try again.

`clean` removes previous build output, and `shadowJar` packages the application and any runtime dependencies
into **`build/libs/Vani.jar`**.
The [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) downloads the project's
configured Gradle version on first use, so you do not need to install Gradle separately.
The first build needs an internet connection to download Gradle and the Shadow plugin.

## Running Vani

From the project root, run:

```text
java -jar build/libs/Vani.jar
```

You can also copy `Vani.jar` into another folder, open a terminal there, and run:

```text
java -jar Vani.jar
```

Vani stores tasks in `data/vani.txt` relative to the folder where you launch it.
Use the same launch folder to keep using the same saved task list.

## Running from source

On Windows PowerShell:

```powershell
.\gradlew.bat run
```

On macOS or Linux:

```sh
./gradlew run
```

To run from an IDE, open the project as a Gradle project, set its Gradle JVM to JDK 25,
and run the `main` method in `src/main/java/vani/Vani.java` with the project root as the working directory.
