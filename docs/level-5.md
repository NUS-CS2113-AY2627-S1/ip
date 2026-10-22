# Level-5: Handle Errors

King reports invalid commands using `KingException` and continues accepting
input. Rejected commands leave the task list and completion status unchanged.

Supported commands:

- `todo <description>`
- `deadline <description> /by <date/time>`
- `event <description> /from <start> /to <end>`
- `list`
- `mark <task number>` and `unmark <task number>`
- `bye`

Command words are case-insensitive. Clause markers are lowercase and separated
from surrounding fields by whitespace. Dates and times remain free-form text.

King explains empty descriptions, missing times, missing or repeated clause
markers, reversed event clauses, unknown or blank commands, extra arguments to
`list` and `bye`, and invalid task numbers. It also rejects additions once the
list reaches 100 tasks at Level-5. Level-6 replaces this fixed limit with a
dynamic collection and adds [deletion](level-6.md). End-of-input exits cleanly even without `bye`.

## Run the regression tests

Use JDK 25. With `java` and `javac` pointing to that version, run from the
repository root in PowerShell:

```powershell
New-Item -ItemType Directory -Force target/level-5-tests | Out-Null
$javaSources = @(Get-ChildItem -Path src/main/java, src/test/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac -d target/level-5-tests $javaSources
java -cp target/level-5-tests king.KingTest
```

The tests use Java's standard library and throw `AssertionError` on failure;
no test framework or `-ea` flag is required. They cover recovery after invalid
commands, preservation of task state, valid Level-4 commands, full-list
boundaries, and end-of-input handling.
