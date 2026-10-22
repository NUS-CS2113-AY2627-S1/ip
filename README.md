# Bill

Bill is a command-line task tracker for to-dos, deadlines, and events. It can find tasks by description, track which ones are done, and save your list between sessions.

[Read the User Guide](docs/README.md) · [Visit the product website](https://bealng.github.io/ip/)

## Run Bill

Install **Java 25**. To use the packaged app, download `bill.jar` from the
[latest release](https://github.com/Bealng/ip/releases/latest), open a terminal in
the folder containing the JAR, and run `java -jar bill.jar`. Bill saves your tasks
relative to that folder, so run it from the same folder each time.

To run Bill from source instead, use the following from the project folder:

| System | Command |
| --- | --- |
| Windows | `./gradlew.bat run` |
| macOS or Linux | `./gradlew run` |

Alternatively, open the project in IntelliJ IDEA, select JDK 25, and run `bill.Bill.main()`.

Type `help` in Bill to see its commands. The User Guide explains every feature, date formats, and how task numbers work.

## Test

Run `./gradlew.bat test` on Windows or `./gradlew test` on macOS or Linux.

Bill saves tasks to `data/bill.txt` in the folder from which it is run. This personal data file is not part of the repository.
