# Dobby project template

This is a project template for a greenfield Java project. It is named _Dobby_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/dobby/Dobby.java` file, right-click it, and choose `Run Dobby.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
        *        .        *        .        *
         ____          _      _
        |  _ \   ___  | |__  | |__   _   _
        | | | | / _ \ | '_ \ | '_ \ | | | |
        | |_| || (_) || |_) || |_) || |_| |
        |____/  \___/ |_.__/ |_.__/  \__, |
                                     |___/
        .        *        .        *        .
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Creating an executable JAR

The project uses the Shadow Gradle plugin to package the application as an executable JAR.
From the project root, run the Gradle wrapper:

```powershell
.\gradlew.bat shadowJar
```

The generated file is `build/libs/dobby.jar`. To run it, copy that JAR into an empty
folder, open a command window in the folder, and run:

```powershell
java -jar "dobby.jar"
```

The JAR is a generated build artifact and should not be committed to Git. The `build/`
directory is already ignored by this repository.
