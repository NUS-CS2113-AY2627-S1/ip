# Noah

Noah is a command-line task manager for todos, deadlines, and events.
Your trusted companion for the next quest.

[User Guide](https://littlecrawler.github.io/ip/) ·
[Download Noah](https://github.com/littlecrawler/ip/releases/latest)

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/noah/Noah.java` file, right-click it, and choose `Run Noah.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
   ...
   NNNNNNNN        NNNNNNNN                                  hhhhhhh
   N:::::::N       N::::::N                                  h:::::h
   N::::::::N      N::::::N                                  h:::::h
   N:::::::::N     N::::::N                                  h:::::h
   N::::::::::N    N::::::N   ooooooooooo     aaaaaaaaaaaaa   h::::h hhhhh
   N:::::::::::N   N::::::N oo:::::::::::oo   a::::::::::::a  h::::hh:::::hhh
   N:::::::N::::N  N::::::No:::::::::::::::o  aaaaaaaaa:::::a h::::::::::::::hh
   N::::::N N::::N N::::::No:::::ooooo:::::o           a::::a h:::::::hhh::::::h
   N::::::N  N::::N:::::::No::::o     o::::o    aaaaaaa:::::a h::::::h   h::::::h
   N::::::N   N:::::::::::No::::o     o::::o  aa::::::::::::a h:::::h     h:::::h
   N::::::N    N::::::::::No::::o     o::::o a::::aaaa::::::a h:::::h     h:::::h
   N::::::N     N:::::::::No::::o     o::::oa::::a    a:::::a h:::::h     h:::::h
   N::::::N      N::::::::No:::::ooooo:::::oa::::a    a:::::a h:::::h     h:::::h
   N::::::N       N:::::::No:::::::::::::::oa:::::aaaa::::::a h:::::h     h:::::h
   N::::::N        N::::::N oo:::::::::::oo  a::::::::::aa:::ah:::::h     h:::::h
   NNNNNNNN         NNNNNNN   ooooooooooo     aaaaaaaaaa  aaaahhhhhhh     hhhhhhh
   ...
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Acknowledgements

OpenAI Codex was used by the project author as a coding assistant across multiple increments.
Its use included generating and revising portions of the Java implementation, refactoring command handling,
implementing and reviewing error handling, organizing classes into packages, creating project-specific skills
and UI tests, and assisting with code review, testing, and Git guidance. The project author reviewed, understood,
and tested all AI-assisted changes.
