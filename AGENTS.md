# Project context

This repository holds **CortisolBot**, a command-line task-tracking chatbot written in Java. It
began as the starter template for an introductory software engineering course and is being built up
by the student who owns the repo, one graded increment at a time. `Level-0` through `Level-9` are
done, as are `A-Classes`, `A-CodingStandard`, `A-CodeQuality`, `A-Jar`, `A-MoreOOP`, `A-JavaDoc` and
`A-UserGuide`; each is merged into `master` and tagged. Later increments carry on from there.

The work is deliberately incremental: each increment is a small, self-contained improvement that is
committed, tagged, and pushed before the next one begins. Prefer the smallest change that completes
the increment at hand over a larger change that anticipates later ones.

# Default user context

Unless the user says otherwise, assume that you are assisting the student who owns this repository.
If the user identifies themselves as an instructor or another project stakeholder, adapt your
response to that role.

# Student profile

* **A confident programmer in their third undergraduate year.** Python and C are the main languages,
  with competitive-programming experience in C++. **Java is new with this course**, and has not been
  difficult. So explain Java-specific things — checked exceptions, generics, `Optional`, the standard
  library, Javadoc conventions, why a class is laid out as it is — and take general programming ideas
  as read. Do not explain what a loop or a class is.
* **Comfortable with Git and GitHub from regular use**, but the *direction* of merges and pulls reads
  backwards easily. Whenever proposing either, say plainly which branch receives the change and which
  one is being read from, rather than leaving it to the command's argument order.
* Operating system: Windows 11, PowerShell.
* **Editor: VS Code for everything.** IntelliJ IDEA is installed and the repo still carries `.idea/`
  and `ip.iml`, but it is barely used — give terminal commands, not IDE instructions. Debugging is
  done with print statements rather than a debugger, so advice that depends on breakpoints or an
  IDE's inspector will not land.

# Current state of the code

All source lives in `src/main/java` as a single default package — keep that folder as the source
root, since tools such as Gradle expect it there.

| File | Role |
|---|---|
| `CortisolBot.java` | Entry point. Holds the Ui, Storage and TaskList, and runs the read-and-execute loop. |
| `Parser.java` | Turns a typed line into a `Command`. Reads command words, task numbers, search keywords, and the `/by` `/from` `/to` markers. Static methods only. |
| `Command.java` | Abstract base: `execute(TaskList, Ui, Storage)` and `isExit()`. |
| `AddCommand.java`, `MarkCommand.java`, `UnmarkCommand.java`, `DeleteCommand.java`, `FindCommand.java`, `ListCommand.java`, `ExitCommand.java` | One class per instruction. Each command that alters the list saves it itself; `find` and `list` do not, since they change nothing. |
| `Ui.java` | Every read from the keyboard and every write to the screen, including the banner and the separator. |
| `TaskList.java` | The tasks, and the only place that maps a task number the user typed onto a position in the list. |
| `Storage.java` | Loads and saves the task list at `data/cortisolbot.txt`. Throws `CortisolException` rather than printing. |
| `Task.java` | Base task: description, done-status, file encoding, keyword matching. |
| `ToDo.java`, `Deadline.java`, `Event.java` | The three task types. |
| `TaskDateTime.java` | A deadline's date, with an optional hour. Owns all three date formats: the one typed, the one displayed, and the one stored. |
| `CortisolException.java` | Errors whose messages are already phrased for the user. |

Two invariants came out of the `A-MoreOOP` increment and are worth preserving:

* **Nothing outside `Ui` touches `System.out` or `Scanner`.** A grep for either outside that file should come back empty.
* **Nothing outside `TaskList` converts a task number into a list index.** The off-by-one lives in one place.

Supported commands: `list`, `find …`, `todo …`, `deadline … /by …`, `event … /from … /to …`, `mark`,
`unmark`, `delete`, `bye`.

A deadline's `/by` must be a real date — `2019-12-02`, or `2019-12-02 1800` when the hour matters —
and anything else is refused. An event's `/from` and `/to` are still free text, and become dates in
some later increment.

No part of a task may contain `|`, since that is what `Storage` divides its fields with. `Parser`
refuses it as the task is typed rather than letting the file take something it could not read back.

Tests live in `test/`:

| File | Role |
|---|---|
| `test/ui-test-plan.md` | The test cases: for each, its aim, the lines typed, and the output expected. |
| `test/run-ui-tests.py` | Runs the plan. Standard library only; no packages to install. |

The product website lives in `docs/`, published by GitHub Pages at
https://PoonnatW.github.io/ip/ from `master` and the `/docs` folder:

| File | Role |
|---|---|
| `docs/README.md` | The user guide, and the page GitHub Pages serves as the site index. |
| `docs/Ui.png` | Terminal screenshot shown at the top of the guide. |
| `docs/_config.yml` | Jekyll settings: the site's title, tagline and theme. |

Every command and message quoted in the user guide was copied from a real session rather than
written from memory. When a user-facing string changes, the guide needs the same pass as the test
plan.

# Project-specific requirements

## Java version

Use Java 25 for running and building. Java 25 is already the default `java` on this machine, so no
version switching is normally needed; verify with `java -version` if something looks off. (On macOS
the course suggests `sdk use java 25.0.3.fx-zulu`, which does not apply here.)

## Build and run

There is no build tool in this repository — no `build.gradle`, no Maven. Compile and run directly:

```powershell
javac -d bin src/main/java/*.java
java -cp bin CortisolBot
```

To build the jar that the GitHub release distributes:

```powershell
javac -encoding UTF-8 -d bin src/main/java/*.java
jar --create --file CortisolBot.jar --main-class CortisolBot -C bin .
```

`--main-class` is what lets a reader run `java -jar CortisolBot.jar` without naming the class.

`bin/`, `out/`, `data/`, and `*.jar` are all gitignored, so build output and the user's saved task
list never enter a commit. An unmerged `origin/add-gradle-support` branch exists if a build tool is
ever wanted; do not merge it without asking.

## Testing — required after every code change

The repository has no unit tests. Its only regression safety net is the text-UI test session:

```powershell
python test/run-ui-tests.py
```

**After any change to `src/main/java`, without exception:**

1. Update `test/ui-test-plan.md` if the change altered or added anything the user can see — a new
   command, a reworded message, a different layout. The plan change belongs in the **same commit** as
   the code change, so that the two never disagree in the repository's history.
2. Invoke the `test-ui` skill, which runs the session and reports it.
3. Show the user the record of the session — the input typed and the output produced — not merely a
   verdict. The script also writes it to `_temp/ui-test-session.txt`.

This applies to refactoring just as much as to new features: a refactoring that changes visible
behaviour has gone wrong, and the session is how that is found out.

A failing test case means either the code is wrong, or the behaviour changed deliberately and the
plan is now out of date. Settle which it is and fix that one. Never edit an expected output merely to
make a test case pass, and never write an expected output by pasting in what the program actually
printed — reason it out from the source and from the voice and formatting rules below.

### Do not fall back to something easier

If the runner itself will not run — a broken plan file, a Python error, a path that has moved — **fix
the runner or the plan**. Do not substitute a hand-run of the program and an eyeballed comparison,
and do not declare the behaviour verified on the strength of a partial run. The whole value of the
session is that it is exact and repeatable; an improvised check that happens to pass is worth less
than no check, because it reads like one in the transcript.

The same goes for the checks around it. Say which command produced a result, and if a command errored
or a check did not actually run, say so rather than reporting the conclusion you expected from it. A
grep that failed on its own arguments has told you nothing.

## The bot's voice — a wealthy household's butler

CortisolBot speaks as an impeccably trained, expensively employed butler: formal, deferential,
unhurried, and dryly witty. **Every** user-facing string must be in this voice. There is no
exemption for errors, confirmations, or incidental system messages — a butler does not drop
character because something went wrong.

Rules for any string the user can see:

* Address the user as `sir/madam`.
* Full sentences and proper punctuation. Contractions (`I've`, `I shall`) are welcome; clipped
  fragments and exclamations are not.
* Dry understatement over enthusiasm. At most one flourish per message, and keep messages to one or
  two lines — the butler is composed, not chatty.
* **Never write `task(s)` or `line(s)`.** The butler is speaking aloud and nobody says
  "task-bracket-s". Where a message carries a count, phrase it so that one and many both read
  correctly — `That makes 1 in your keeping`, `I have retrieved 1 of your tasks` — rather than
  adding pluralization logic or an `(s)`.
* Errors explain the misunderstanding in character, then offer the correct form, e.g.
  `Do try: deadline <description> /by 2019-12-02`. Where a format is strict, show a real example
  rather than a placeholder — `<when>` would promise a flexibility the parser does not have.
* **Never ship the stock wordings from the course's starter material** — `"Got it. I've added this
  task:"`, `"Nice!"`, `"OK, I've marked this task as not done yet:"`, `"Noted. I've removed this
  task:"`, `"Here are the tasks in your list:"`. Rewrite them in the butler voice.

Lines already in the code that set the standard:

```
Greetings sir/madam, CortisolBot humbly at your service.
Your list is presently empty, sir/madam. A rare luxury.
A todo without a description is rather like tea without leaves, sir/madam.
I do beg your pardon, sir/madam, but that instruction is not in my repertoire.
Tonight has been an honour, sir/madam. I shall bid you good evening.
Very good, sir/madam. I have added the following:
Consider it done, sir/madam:
Very well, sir/madam. I have returned it to the undone:
Consider it forgotten, sir/madam:
Your tasks, sir/madam, as they presently stand:
```

The replies that replaced the stock wordings share a house style on purpose: `Consider it done` /
`Consider it forgotten` are a matched pair, and both counts read `That makes 3 in your keeping.` /
`That leaves 1 in your keeping.` — phrased that way so that one task reads correctly too, which the
stock `Now you have 1 tasks in the list.` did not. Keep new strings inside that style rather than
inventing a sixth register.

When you add or change a user-facing string, check it against those. If the right phrasing is not
obvious, propose the wording to the user rather than guessing.

## Output formatting

These details are part of the bot's presentation and are what expected-output tests compare against,
so match them exactly rather than inventing new layout:

* A separator line of 55 hyphens closes each exchange.
* Error messages are printed with a single leading space.
* Task lines inside a response are indented with a tab.

## Git

* One branch per increment, named `branch-<Increment>` (e.g. `branch-A-MoreOOP`), merged into
  `master` with a merge commit. Non-increment chores may go straight onto `master`.
* **Every commit message must follow the `seedu-git-standard` skill.** Read it before proposing one.
  In short, the required shape is `<category>: <Capitalized imperative phrase>` — no trailing
  period, 72 characters maximum and ideally 50.
* Commit messages are **a single sentence — subject line only, no body**. Keep them short enough to
  read at a glance in `git log --oneline`. The rationale for a change belongs in your chat
  explanation and in the code's own comments, not in the commit message.
* Categories in use: `feat:`, `refactor:`, `style:`, `chore:`; `fix:` and `docs:` when they apply.
* **One commit per standalone change.** Code changes and agent-file changes never share a commit.
* Use lightweight tags (`git tag A-MoreOOP`) unless an annotated tag is requested. One tag per
  increment, placed on `master` after the merge.
* The remote is `origin` → `https://github.com/PoonnatW/ip.git`.

### The user runs all Git commands — you never do

**Do not execute Git commands yourself.** No `git add`, `commit`, `tag`, `merge`, `push`,
`checkout`, `branch`, `reset`, or `stash` — not even when the user says "commit this", and not even
when a command appears to be pre-approved. The user wants to inspect every change before it enters
the repository's history.

Read-only inspection is the one exception: `git status`, `git diff`, `git log`, `git show`, and
`git branch --list` are fine to run on your own when you need to understand the current state.

When a Git action is needed, **put the exact commands in a copy-pasteable block at the very end of
your message**, after the explanation, so the user can review them and run them. Then stop and wait
— do not assume the commands were run, and do not continue with work that depends on them until the
user confirms. Since commit messages are one sentence, a plain `-m "…"` is always enough — never
reach for a here-string or heredoc.

Format it like this:

~~~
```powershell
git checkout -b branch-A-MoreOOP
git add src/main/java/Ui.java src/main/java/CortisolBot.java
git commit -m "refactor: Extract user interaction into a Ui class"
```
~~~

Explain briefly what any unfamiliar command does, per the guidance below. If the user reports that a
command failed, diagnose it and propose a corrected command — still for them to run.

# Project skills — mandatory, not optional

Project skills live in `.claude/skills/<name>/SKILL.md` and are committed to the repo so they are
available in every session. Read the relevant skill *before* doing the work it governs, not after.

| Skill | When it is mandatory |
|---|---|
| `seedu-java-coding-standard` | Writing, reviewing, or reformatting **any** Java code in this project. |
| `seedu-git-standard` | Proposing or reviewing **any** commit message, and naming any branch. |
| `present-changes-visually` | Presenting the result of an increment, or whenever the user asks to see changes. |
| `test-ui` | After **any** change to `src/main/java`, and whenever the user asks for the bot to be tested. |

These are standards, not suggestions: code that does not follow `seedu-java-coding-standard` is not
finished, a commit message that does not follow `seedu-git-standard` should not be proposed, and a
code change that has not been through `test-ui` is not ready to be committed.

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For
  example:
  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes, and to nontrivial methods and fields whose
    purpose or behaviour is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they
    improve understanding.
  * When faced with a design choice, choose the simplest option sufficient for the requirements,
    while briefly explaining relevant more advanced alternatives.

# Known gaps

Honest record of what is missing, so it is not mistaken for something that already works:

* **No unit tests.** Every class is exercised only from the outside, through the text-UI test session
  in `test/ui-test-plan.md` (27 test cases, run by the `test-ui` skill). That session covers the
  commands, the error messages and the data file, but it cannot reach a method that no command calls,
  and it says nothing about how the code is arranged inside. JUnit is the course's answer and has not
  been introduced yet.
* **`.gitignore` still mentions `text-ui-test/`**, which is the course template's harness layout. This
  repository never rebuilt that folder and uses `test/` instead; the stale lines are harmless and have
  been left alone.
* **`Storage` still does not escape its field separator.** Typing a `|` into a task is refused by
  `Parser` (TC-17), so the file can no longer be corrupted through the UI — but a file edited by hand
  can still carry one inside a field, and comes back truncated rather than reported as unreadable
  (TC-18). Escaping on write would close it properly, and belongs in whichever increment next touches
  `Storage`.
* **The released jar can fall behind the code.** `CortisolBot.jar` is gitignored and distributed
  through GitHub releases, so nothing rebuilds it automatically. The user guide tells readers to
  download it, which means a release that predates the newest increment hands them a product that
  does not match the guide. Rebuild and attach a fresh jar whenever a visible feature lands.
* **`data/cortisolbot.txt` in the repository root still holds a pre-Level-8 task list**, including a
  deadline written as free text. Running the bot from the repository root reports it as unreadable
  and drops it on the first save. Harmless but surprising; move the file aside before any demo or
  screenshot taken from the root.
