# Level 9: Find tasks

Use `find <keyword>` to search task descriptions:

```text
find book
    Here are the matching tasks in your list:
    1. [T][X] read book
    2. [D][ ] return book (by: Oct 15 2019)
```

Matching uses a case-sensitive literal substring: `book` also matches
`notebook`, but does not match `Book`. The command name is case-insensitive.
Multiple words are treated as one phrase. Dates and event times are excluded
unless they are part of the task's original description.

Results retain task order and are numbered from 1 within the results.
Use `list` to see the original task numbers before marking or deleting.
An empty result displays `No matching tasks found.` A missing keyword
shows a usage message. Finding tasks changes neither tasks nor the save file.
