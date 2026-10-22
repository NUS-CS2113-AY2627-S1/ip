# Ducky User Guide

**Quack! Ducky at your service.** 🦆

Ducky is a friendly little chatbot that lives in your terminal. It keeps track of your chores (tasks) for you, remembers dates and deadlines, and helps you find the chore you need when you need it.

## Quick start

1. Make sure you have **Java 25** installed. (Check with `java -version`.)
2. Download the latest `ducky.jar` from the [Releases](https://github.com/wunicorn21/ip/releases) page.
3. Put it in any folder, open a terminal there, and run:
   ```
   java -jar ducky.jar
   ```
4. Ducky quacks hello. Type a command and press Enter. Try `todo feed the ducks`!

---

## Features

> **Before you paddle in:**
> * Words in `UPPER_CASE` are things you fill in, e.g. in `todo DESCRIPTION`, try `todo read book`.
> * Commands are in lowercase: `list` works, `LIST` doesn't.
> * Chore numbers (`INDEX`) are the numbers shown by `list`, starting from 1.

### Adding a to-do: `todo`

A simple chore with no date attached. Just something to get done.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
QUACKDDING! I've added this chore:
  [T][ ] read book
Now you have 1 chores in the list.
```

### Adding a deadline: `deadline`

A chore that must be done **by** a certain date. Ducky checks that the date is a real calendar date, so something like `31-04-2026` is rejected.

Format: `deadline DESCRIPTION /by DD-MM-YYYY`

Example: `deadline return book /by 15-10-2026`

```
QUACKDDING! I've added this chore:
  [D][ ] return book (by: 15 Oct 2026)
Now you have 2 chores in the list.
```

### Adding an event: `event`

A chore that happens **from** one time **to** another. The times can be any text you like.

Format: `event DESCRIPTION /from START /to END`

Example: `event project meeting /from Mon 2pm /to 4pm`

```
QUACKDDING! I've added this chore:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 chores in the list.
```

### Listing all chores: `list`

Shows every chore in your pond.

Format: `list`

```
[T][ ] 1. read book
[D][ ] 2. return book (by: 15 Oct 2026)
[E][ ] 3. project meeting (from: Mon 2pm to: 4pm)
```

How to read it: the first box is the chore type (`T` to-do, `D` deadline, `E` event). The second box shows `X` when the chore is done.

### Marking a chore as done: `mark`

Finished something? Let Ducky celebrate with you.

Format: `mark INDEX`

Example: `mark 1`

```
done quacking read book
```

### Marking a chore as not done: `unmark`

Changed your mind? Ducky can un-quack it.

Format: `unmark INDEX`

Example: `unmark 1`

```
oh! actl im not done quaking read book
```

### Deleting a chore: `delete`

Removes a chore for good, then shows what's left.

Format: `delete INDEX`

Example: `delete 3`

```
[E][ ] 3. project meeting (from: Mon 2pm to: 4pm) waddled away!
Now chorelist is:
[T][ ] 1. read book
[D][ ] 2. return book (by: 15 Oct 2026)
Now you have 2 chores in the list.
```

### Finding chores: `find`

Shows every chore whose description contains your keyword. The search is **case-sensitive**, so `book` won't match `Book`.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching chores in your list:
[T][ ] 1. read book
[D][ ] 2. return book (by: 15 Oct 2026)
```

### Exiting: `bai`

Says goodbye and closes Ducky.

Format: `bai`

```
OK BAI. OFF TO BUY SOME LEMONADE
```

### Saving your chores

No need to save by hand! Ducky saves your chores every time they change, and loads them again the next time you start it. They're kept in `data/ducky.txt`, in the folder you run Ducky from.

> ⚠️ Editing `data/ducky.txt` yourself isn't recommended. If Ducky finds a line it can't read, it skips that line, tells you how many it skipped, and removes them the next time it saves.

---

## Command summary

| Command    | Format                                    | Example                                      |
|------------|-------------------------------------------|----------------------------------------------|
| To-do      | `todo DESCRIPTION`                        | `todo read book`                             |
| Deadline   | `deadline DESCRIPTION /by DD-MM-YYYY`     | `deadline return book /by 15-10-2026`        |
| Event      | `event DESCRIPTION /from START /to END`   | `event project meeting /from Mon 2pm /to 4pm`|
| List       | `list`                                    | `list`                                       |
| Mark       | `mark INDEX`                              | `mark 1`                                     |
| Unmark     | `unmark INDEX`                            | `unmark 1`                                   |
| Delete     | `delete INDEX`                            | `delete 3`                                   |
| Find       | `find KEYWORD`                            | `find book`                                  |
| Exit       | `bai`                                     | `bai`                                        |
