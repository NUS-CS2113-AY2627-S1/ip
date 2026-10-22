# King project template

This is a project template for a greenfield Java project. Given below are instructions on how to use it.

## Run the executable JAR

Download `King.jar` from [GitHub releases](https://github.com/YamYamui/ip/releases).
With Java 25 installed, copy it into an empty folder, open a terminal there,
and run `java -jar "King.jar"`.
See [A-Jar instructions](docs/a-jar.md) for building and distributing the JAR.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/king/King.java` file, right-click it, and choose `Run King.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
       __    _          
      / /__ (_)__  ___ _
     /  '_// / _ \/ _ `/
    /_/\_\/_/_//_/\_, / 
                 /___/  
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Package organization

- `king`: the chatbot entry point, `King`.
- `king.task`: `Task` and its `ToDo`, `Deadline`, and `Event` subclasses.
- `king.exception`: the recoverable command error, `KingException`.
- `king.storage`: saving and loading task data through `Storage`.

Packages group related classes under the project name. Their folders sit beneath
`src/main/java`, which remains the source root. Regression tests mirror the
`king` package under `src/test/java`.

## Run from PowerShell

With `java` and `javac` pointing to JDK 25, run from the repository root:

```powershell
New-Item -ItemType Directory -Force target/classes | Out-Null
$javaSources = @(Get-ChildItem -Path src/main/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac -d target/classes $javaSources
java -cp target/classes king.King
```

Use the fully qualified class name `king.King` when launching the application.
See [Level-5 regression tests](docs/level-5.md#run-the-regression-tests) for test commands.

King saves tasks automatically to `data/king.txt` and restores them on startup.
See [Level-7 storage](docs/level-7.md) for the file format, error handling, and
persistence test commands.
