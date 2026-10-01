/**
 * The core of the Floppy chatbot: the {@link floppy.Floppy} class that runs the
 * conversation, and the pieces it coordinates.
 *
 * <p>Each piece has one job. {@link floppy.Ui} reads the user's commands and prints
 * every reply, {@link floppy.Parser} makes sense of what was typed,
 * {@link floppy.TaskList} holds the tasks, and {@link floppy.Storage} keeps them on
 * disk between runs. Anything Floppy cannot do as asked is reported as a
 * {@link floppy.FloppyException}.
 */
package floppy;
