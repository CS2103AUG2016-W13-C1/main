package seedu.todo.logic.commands;

import seedu.todo.commons.core.Config;
import seedu.todo.commons.core.EventsCenter;
import seedu.todo.commons.core.Messages;
import seedu.todo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.todo.model.Model;
import seedu.todo.storage.Storage;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected Config config;
    protected Storage storage;
    
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setModel(Model model) {
        this.model = model;
    }
    
    public void setConfig(Config config) {
        this.config = config;
    }
    
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent());
    }
}
