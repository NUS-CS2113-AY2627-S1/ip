# JamaL

A command-line task manager with no patience. You type a command, it does the thing, it tells you it did the thing.

```
____________________________________________________________
     ____.                     .____
    |    |____    _____ _____  |    |
    |    \__  \  /     \\__  \ |    |
/\__|    |/ __ \|  Y Y  \/ __ \|    |___
\________(____  /__|_|  (____  /_______ \
              \/      \/     \/        \/
It's JamaL.
What do you want.
____________________________________________________________
```

📖 **[Full User Guide](https://zechby.github.io/ip/)**

## What it does

- Tracks three kinds of tasks: **todos**, **deadlines**, and **events**
- Marks tasks done or not done, deletes them, and finds them by keyword
- Saves after every change to `data/jamal.txt`, so there's no save command to forget
- Skips broken lines in the save file instead of crashing, and tells you how many it skipped

## Quick start

1. Install **Java 25** (`java -version` to check).
2. Download the latest `jamal.jar` from [Releases](https://github.com/zechby/ip/releases).
3. Put it in an empty folder, open a terminal there, and run:

   ```
   java -jar jamal.jar
   ```

4. Type a command. Type `bye` to leave.

## Commands

| Command  | Format                                  | Example                                       |
| -------- | --------------------------------------- | --------------------------------------------- |
| Todo     | `todo DESCRIPTION`                      | `todo read book`                              |
| Deadline | `deadline DESCRIPTION /by WHEN`         | `deadline return book /by Sunday`             |
| Event    | `event DESCRIPTION /from START /to END` | `event project meeting /from Mon 2pm /to 4pm` |
| List     | `list`                                  | `list`                                        |
| Mark     | `mark INDEX`                            | `mark 2`                                      |
| Unmark   | `unmark INDEX`                          | `unmark 2`                                    |
| Delete   | `delete INDEX`                          | `delete 3`                                    |
| Find     | `find KEYWORD`                          | `find book`                                   |
| Exit     | `bye`                                   | `bye`                                         |

`INDEX` is the number shown in `list`, starting from 1. Task text can't contain `|`.

Get a command wrong and JamaL tells you what it wanted:

```
> deadline essay
deadline what /by when.

> dance
I'm not doing whatever dance is.
```

See the [User Guide](https://zechby.github.io/ip/) for full output samples, the save file format, and every error message.

## Building from source

Requires JDK 25.

```
git clone https://github.com/zechby/ip.git
cd ip
./gradlew clean shadowJar
```

The JAR ends up in `build/libs/`. On Windows, use `gradlew.bat` instead of `./gradlew`.

Run the tests:

```
./gradlew test
```

## Acknowledgements

Built as the individual project (iP) for [NUS CS2113](https://nus-cs2113-ay2627-s1.github.io/website/index.html), starting from the course's [Duke template](https://github.com/NUS-CS2113-AY2627-S1/ip).
