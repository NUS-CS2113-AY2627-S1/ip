# DINO User Guide

DINO is a simple chatbot that helps you keep track of your tasks.

## Adding a todo

Use `todo` followed by the task description.

Example:
`todo read book`

## Adding a deadline

Use `deadline` followed by the task description and `/by` followed by the deadline date.

The date must be in `yyyy-MM-dd` format.

Example:
`deadline return book /by 2026-10-10`

## Adding an event

Use `event` followed by the event description, `/from` followed by the start time, and `/to` followed by the end time.

Example:
`event project meeting /from Monday /to Tuesday`

## Viewing your tasks

Use `list` to display all your tasks.

Example:
`list`

## Marking a task as done

Use `mark` followed by the task number.

Example:
`mark 2`

## Marking a task as not done

Use `unmark` followed by the task number.

Example:
`unmark 2`

## Deleting a task

Use `delete` followed by the task number.

Example:
`delete 2`

## Finding tasks

Use `find` followed by a keyword to display tasks containing that keyword.

Example:
`find book`

## Exiting DINO

Use `bye` to exit DINO.

Example:
`bye`