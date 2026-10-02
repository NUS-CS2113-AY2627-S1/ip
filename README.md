# Foodielover

**Foodielover** is a desktop task management application optimized for users who prefer working through a Command Line Interface (CLI). Designed for students, professionals, and food lovers alike, it allows managing tasks, deadlines, and events faster than traditional GUI-based apps.

---

## Documentation

* [User Guide](https://siewhean.github.io/ip/) - Comprehensive documentation detailing all commands, options, and usage examples.

---

## Key Features

* **Task Variety**: Manage `todo`, `deadline`, and `event` tasks with ease.
* **Flexible Date/Time Understanding**: Parses both standard ISO dates (`yyyy-MM-dd`, `dd/MM/yyyy`, optional 24-hr time) and natural language text (e.g., `June 6th`, `tonight`).
* **Strict Validation**: Rejects invalid dates (e.g., `2019-02-30`) and ensures chronological order for event timings (`/to` after `/from`).
* **Find & Filter**: Search tasks by keyword (`find KEYWORD`) or filter tasks scheduled on a specific date (`date DATE` / `on DATE`).
* **Safe Persistence**: Automatically saves tasks to a local data file (`data/foodielover.txt`) and creates automatic backups (`data/foodielover.txt.bak`) before modifying saved data.

---

## Quick Start

1. Ensure **Java 25** is installed on your computer.
2. Download the latest `foodielover.jar` from the [Releases](https://github.com/siewhean/ip/releases) page.
3. Open a terminal in the folder containing `foodielover.jar`.
4. Run the application:
   ```bash
   java -jar foodielover.jar
   ```

---

## Developer Guide

### Prerequisites
* JDK 25
* IntelliJ IDEA (recommended) or any Java IDE supporting Gradle

### Building and Testing
Build the standalone executable JAR:
```bash
./gradlew clean shadowJar
```
The generated JAR will be located at `build/libs/foodielover.jar`.

Run automated JUnit tests:
```bash
./gradlew test
```

Run UI regression tests:
```bash
python3 .agents/skills/test-ui/scripts/run_ui_tests.py
```

