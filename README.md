# Proton project template

This is a project template for a greenfield Java project. It's named after the Java mascot _Duke_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Duke.java` file, right-click it, and choose `Run Duke.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Task persistence

Run Proton from the project root. Proton loads `data/proton.txt` at startup
and saves after adding, marking, unmarking, or deleting a task. A missing
file starts an empty list. Malformed or unreadable saves stop startup before
commands can modify the data; repair the file or restore a backup and restart.

New saves start with `PROTON 1`. Each following line contains tab-separated
task type (`T`, `D`, or `E`), completion status (`0` or `1`), and task fields
encoded as Base64 UTF-8. Encoding keeps tabs, Unicode, and display markers
inside fields separate from the file structure. Base64 is not encryption.
Deleting the last task leaves only the header. Empty legacy files and valid
legacy display-format records still load; the next successful edit converts
them to the new format. Ambiguous legacy records must be corrected manually.

Proton writes a temporary file in `data/`, then atomically replaces the save.
If writing or replacement fails, the command is rejected and the in-memory
list is restored. The old save is not truncated. Check available disk space,
path permissions, and whether another program is holding the file open before
retrying. File systems without atomic replacement reject saves safely.

Changes made to the file since loading are detected before saving. Restart
Proton to load those changes. Use one Proton instance at a time: this check
is not a lock against simultaneous writers. Symbolic links at the data or
save path are rejected. Temporary files left by an interrupted write are not
loaded. Atomic replacement protects against partial writes, but does not
replace backups or guarantee durability after hardware failure or power loss.

## Console tests

With Java 25 and Python 3.10 or newer, run `python test/run_ui.py` from the
project root. Tests use isolated directories under `_temp/`, preserving real
saved tasks. See `test/ui-test-plan.md` for exact inputs, expected outputs,
file-system fixtures, and the latest captured session.
