# Cracker project template

This is a project template for a greenfield Java project. The chatbot is named _Cracker_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/cracker/Cracker.java` file, right-click it, and choose `Run Cracker.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, use `todo DESCRIPTION`, `deadline DESCRIPTION /by DUE_TIME`, or `event DESCRIPTION /from START_TIME /to END_TIME` to add tasks, use `list` to show all tasks, use `mark TASK_NUMBER` or `unmark TASK_NUMBER` to update a task's completion status, use `delete TASK_NUMBER` to remove a task, and enter `bye` to exit. Dates and times are stored as the text you enter:
   ```
   ____________________________________________________________
    ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗
   ██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗
   ██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝
   ██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗
   ╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║
    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
   Hello! I'm Cracker.
   What can I do for you?
   ____________________________________________________________
   todo read book
    Got it. I've added this task:
      [T][ ] read book
    Now you have 1 tasks in the list.
   ____________________________________________________________
   deadline return book /by June 6th
    Got it. I've added this task:
      [D][ ] return book (by: June 6th)
    Now you have 2 tasks in the list.
   ____________________________________________________________
   list
    Here are the tasks in your list:
    1.[T][ ] read book
    2.[D][ ] return book (by: June 6th)
   ____________________________________________________________
   mark 2
   Nice! I've marked this task as done:
      [D][X] return book (by: June 6th)
   ____________________________________________________________
   unmark 2
    OK, I've marked this task as not done yet:
      [D][ ] return book (by: June 6th)
   ____________________________________________________________
   bye
    Bye. Hope to see you again soon!
   ____________________________________________________________
   ```

Cracker keeps tasks in memory while it is running. The tasks are not saved after the program exits.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
