# Kevie User Guide

A simple CLI application that allows you to keep track of your tasks in an organised way.

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
   - [View Help: `help`](#view-help-help)
   - [Exit: `bye`](#exit-bye)
   - [Add todo task: `todo`](#add-todo-task-todo)
   - [Add deadline task: `deadline`](#add-deadline-task-deadline)
   - [Add event task: `event`](#add-event-task-event)
   - [List all tasks: `list`](#list-all-tasks-list)
   - [Mark task as done: `mark`](#mark-task-as-done-mark)
   - [Reset task as not done: `unmark`](#reset-task-as-not-done-unmark)
   - [Delete task: `delete`](#delete-task-delete)
   - [Find task with keyword: `find`](#find-task-with-keyword-find)
   - [Saving and loading](#saving-and-loading)

## Quick Start
1. Make sure you have Java 25 (or above) installed.
2. Download the latest JAR file in the [releases page](https://github.com/czfray/ip/releases "releases").
3. Move the downloaded JAR file to a directory of your liking.
4. Open the terminal and execute `cd` to the directory with the JAR file
5. Run `java -jar kevie.jar`
   If done correctly, something like the following would show up:
```
====================================
██╗  ██╗███████╗██╗   ██╗██╗███████╗
██║ ██╔╝██╔════╝██║   ██║██║██╔════╝
█████╔╝ █████╗  ██║   ██║██║█████╗
██╔═██╗ ██╔══╝  ╚██╗ ██╔╝██║██╔══╝
██║  ██╗███████╗ ╚████╔╝ ██║███████╗
╚═╝  ╚═╝╚══════╝  ╚═══╝  ╚═╝╚══════╝
====================================
```
6. Type in a command and press enter to execute it.

## Features

### View Help: `help`
Prints out all the commands that Kevie can understand, and their corresponding syntaxes.
Format: `help`

### Exit: `bye`
Terminates Kevie.
Format: `bye`

### Add todo task: `todo`
Creates a simple todo task, and adds it into the todo list.
Format: `todo [Description]`
Example: `todo CS2113 iP bug fix`

### Add deadline task: `deadline`
Creates a task with a deadline, and adds it into the todo list.
Format: `deadline [Description]  /by [Due Time]]`
Example: `deadline CS2113 individual project /by 5th Sep 2359`
There must be spaces before and after `/by` (`deadline abcd/by1pm tmrw` is invalid).

### Add event task: `event`
Creates an event task, and adds it into the todo list.
Format: `event [Description] /from [Start Time] /to [End Time]`
Example: `event CS2113 team project meeting /from Sep 1st 6pm /to Sep 1st 8pm`
There must be spaces before and after `/from` and `/to` (`event abcd/from1pm/to2pm` is invalid).

### List all tasks: `list`
Lists all tasks in the todo list.
Format: `list`

### Mark task as done: `mark`
Mark a task in the todo list as done.
To see what is the `Task No.` of each task, execute `list`.
Format: `mark [Task No.]`
Example: `mark 3`

### Reset task as not done: `unmark`
Mark a task in the todo list back to not done.
To see what is the `Task No.` of each task, execute `list`.
Format: `unmark [Task No.]`
Example: `unmark 3`

### Delete task: `delete`
Delete a task in the todo list.
To see what is the `Task No.` of each task, execute `list`.
Format: `delete [Task No.]`
Example: `delete 3`

### Find task with keyword: `find`
Find all tasks with a specified keyword in the todo list (case-insensitive).
Format: `find [Keyword]`
Example: `find CS2113`

### Saving and loading
- Auto Saving: When tasks in the todo list is added, modified or deleted, the changes would be automatically saved in a file located at `saves/tasks.kv`.
- Auto Loading: The todo list saved would be automatically loaded back into the system when the application is reopened.