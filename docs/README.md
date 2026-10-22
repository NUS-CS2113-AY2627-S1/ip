# Bond User Guide

Bond is a command-line chatbot that helps you keep track of missions (tasks). You can add a simple to-do, a deadline, or an event; view your missions; and update their status. Bond saves changes so your missions are available the next time you start it.

## Getting started

### Run the released JAR

1. Install JDK 25. Open a terminal and run `java -version` to check that Java 25 is available.
2. Download `bond.jar` from the [latest GitHub release](https://github.com/FawazRM23/ip/releases/latest).
3. Put `bond.jar` in an empty folder and open a terminal in that folder.
4. Run:

   ```text
   java -jar bond.jar
   ```

5. Type one command at a time and press Enter. Type `bye` when you are finished.

### Run from source

Open this project in IntelliJ IDEA, set the project SDK to JDK 25, and run `bond.Bond.main()` from `src/main/java/bond/Bond.java`. Enter commands in the Run window.

Commands are lowercase. For commands with details, put a space after the command word. Bond calls tasks **missions** in its responses.

## Add missions

### To-do

Use `todo <description>` for a mission without a date or time. For example:

```text
todo inspect equipment
```

Bond adds the mission and displays it as `[T][ ] inspect equipment`.

### Deadline

Use `deadline <description> /by <yyyy-MM-dd>` for a mission that must be done by a certain date. Enter a real date in year-month-day format. For example:

```text
deadline submit report /by 2026-10-15
```

Bond displays it as `[D][ ] submit report (by: Oct 15 2026)`.

### Event

Use `event <description> /from <start> /to <end>` for a mission with a start and end. For example:

```text
event team briefing /from Monday 9am /to Monday 10am
```

Bond displays it as `[E][ ] team briefing (from: Monday 9am to: Monday 10am)`.

For events, Bond stores the start and end text you enter without checking it. Bond does not send reminders.

## View and manage missions

Type `list` to see every mission and its current number. For example, after adding the three missions above:

```text
1.[T][ ] inspect equipment
2.[D][ ] submit report (by: Oct 15 2026)
3.[E][ ] team briefing (from: Monday 9am to: Monday 10am)
```

`[T]`, `[D]`, and `[E]` identify to-dos, deadlines, and events. `[ ]` means the mission is not done; `[X]` means it is done.

To find missions, type `find <keyword>` (for example, `find report`). Bond searches descriptions without regard to letter case and shows matching missions with their original list numbers. You can search for a phrase too. Dates and event times are not searched. If nothing matches, Bond says so.

Use the number shown by `list` to change a mission:

| Command | What it does |
| --- | --- |
| `mark 2` | Marks mission 2 as done. |
| `unmark 2` | Marks mission 2 as not done again. |
| `delete 2` | Removes mission 2. This cannot be undone in Bond. |

After deleting a mission, use `list` again: the remaining missions are renumbered.

## Leave Bond and keep your missions

Type `bye` to end the session. Bond saves each addition, status change, and deletion automatically in `data/bond.txt` under the directory from which you run the app. It loads that file when you start Bond again, so use the same working directory to see your saved missions.

If a command is incomplete or a mission number does not exist, Bond explains the error and shows how to correct it. You can then enter another command.
