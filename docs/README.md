# Milk

Milk is a chatbot named after the rhythm game mascot! Given below are instructions on how to use it.

## Setting up in Intellij

(You can also skip this and simply run the jar file. Instructions are below.)

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
2. Open the project into Intellij as follows:
   i. Click `Open`.
   ii. Select the project directory, and click `OK`.
   iii. If there are any further prompts, accept the defaults.
3. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
4. After that, locate the `src/main/java/milk/main/Milk.java` file, right-click it, and choose `Run Milk.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    __  __   _   _   _        _   _
   |  \/  | (_) | | | | __   | | | |
   | |  | | | | | | |   <    |_| |_|
   |_|  |_| |_| |_| |_|\_\   (_) (_)
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Using the Jar file

Prerequisites: JDK 25

1. Install the Jar file.
2. Open a terminal and run `java -jar Milk.jar`.
3. On successfully running, you should see something like this below as the output:
   ```
    __  __   _   _   _        _   _
   |  \/  | (_) | | | | __   | | | |
   | |  | | | | | | |   <    |_| |_|
   |_|  |_| |_| |_| |_|\_\   (_) (_)
   ```

## Using the chatbot

1. Use the command `todo a` to create a task with description `a`.
2. Use the command `deadline a /by b` to create a task with description `a` and deadline `b`.
3. Use the command `event a /from b /to c` to create an event with description `a` from `b` to `c`.
4. Use `list` to list out your tasks.
5. Use `mark 1` or `unmark 1` to mark/unmark the first task in the list. You can change the index accordingly.
6. Use `delete 1` to delete the first task in the list. You can change the index accordingly.
7. Use `find a` to find tasks with the phrase `a` in their description. 
8. Exit the chatbot with `bye`.
