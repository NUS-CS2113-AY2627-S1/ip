# UI Test First Failure (Before Fix)

## Handle File at the Data Folder Path: FAIL

Command: `java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "C:\Users\zepht\Documents\CS2113 iP\ip\out" chillguy.Chillguy`

Working directory: isolated sandbox `34`, storage session `34`.

Input:

```text
todo must not overwrite
bye
```

Actual output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
____________________________________________________________
Sorry, I couldn't save tasks to data/chillguy.txt. No changes were made. Check that the data folder is writable and the file is not in use.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Exit code: 0

Stderr: ''

- PASS: storage bytes and paths unchanged
- PASS: no temporary save files remain

Expected output:

```text
____________________________________________________________
   _____ _   _ ___ _     _      _____ _   _ __   __
  / ____| | | |_ _| |   | |    / ____| | | |\ \ / /
 | |    | |_| || || |   | |   | |  __| | | | \ V /
 | |___ |  _  || || |___| |___| | |_ | |_| |  | |
  \____||_| |_|___|_____|______\_____|____/   |_|
Hello! I'm Chillguy.
What can I do for you?
____________________________________________________________
Sorry, I couldn't load tasks from data/chillguy.txt. Check that the path is a readable UTF-8 file.
Your saved file has not been changed.
____________________________________________________________
```
