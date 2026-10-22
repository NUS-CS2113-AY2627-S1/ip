# Future Requirements

This document records possible enhancements for future updates to Nga. Items
can be selected and prioritized as later development levels are defined.

## Core functionality

- Support `delete <number>` and persist the deletion.
- Support `find <keyword>` across task descriptions.
- Decide whether duplicate tasks should be allowed.
- Add a `clear` command to remove all tasks.
- Add filtering or sorting by task type and completion status.
- Support editing an existing task.

## Persistence

- Escape descriptions containing the `|` delimiter.
- Preserve usable task data when the storage file is partially corrupted.
- Display a warning when some records cannot be loaded.
- Create a backup before overwriting the data file.
- Avoid losing in-memory changes if saving fails.
- Define behavior when more than 100 tasks are loaded.

## Input validation

- Reject or safely encode unsupported delimiter characters.
- Handle extra spaces consistently.
- Validate task numbers for every numbered command.
- Reject malformed deadline and event formats with clear guidance.
- Handle end-of-file and blank input gracefully.

## Quality and testing

- Add automated unit tests for `Storage`, `TaskParser`, and `TaskList`.
- Add end-to-end UI tests for save-and-restart behavior.
- Test missing, malformed, unreadable, and unwritable data files.
- Test deleting the first, middle, last, and only task.
- Add a regression test ensuring task order survives a restart.
