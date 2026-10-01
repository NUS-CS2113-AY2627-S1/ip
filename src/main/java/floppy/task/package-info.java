/**
 * The kinds of task Floppy can track.
 *
 * <p>{@link floppy.task.Task} holds what every task has in common -- a description and
 * whether it is done -- and each subclass adds what makes it different:
 * {@link floppy.task.Todo} has no date, {@link floppy.task.Deadline} is due on one,
 * and {@link floppy.task.Event} runs between a start and an end.
 */
package floppy.task;
