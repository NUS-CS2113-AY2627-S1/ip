# Eric project

This is a project template for a greenfield Java project. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/eric/Eric.java` file, right-click it, and choose `Run Eric.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
   ███████╗██████╗ ██╗ ██████╗
   ██╔════╝██╔══██╗██║██╔════╝
   █████╗  ██████╔╝██║██║     
   ██╔══╝  ██╔══██╗██║██║     
   ███████╗██║  ██║██║╚██████╗
   ╚══════╝╚═╝  ╚═╝╚═╝ ╚═════╝

   Hello! I'm Eric.
   What can I do for you?
   ```

## Building and running a JAR file

The project uses Gradle with the `shadow` plugin to build a single "fat" JAR file (`eric.jar`) that contains everything needed to run Eric. JDK 25 is needed.

1. Build it, from the project root: `./gradlew shadowJar` (on Windows: `gradlew.bat shadowJar`). The first build downloads Gradle, so it needs an internet connection.
1. The JAR is created at `build/libs/eric.jar`.
1. Run it with `java -jar build/libs/eric.jar`. Eric saves its tasks in a `data` folder next to where you run the command.

To run Eric without building the JAR, use `./gradlew run`.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
