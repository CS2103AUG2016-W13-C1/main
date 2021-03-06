package seedu.todo.logic.commands;

import seedu.todo.commons.core.Messages;
import seedu.todo.commons.core.UnmodifiableObservableList;
import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.tag.UniqueTagList;
import seedu.todo.model.task.ReadOnlyTask;
import seedu.todo.model.task.UniqueTaskList.TaskNotFoundException;
//@@author A0142421X
/**
 * Tags a task identified using it's last displayed index from the to do list.
 * with tags
 */
public class UntagCommand extends Command{

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Untags the task identified by the index number used in the last task listing. "
            + "Tag names must be unique\n"
            + "Parameters: INDEX TAGNAME [MORE TAGNAMES]\n"
            + "Example: " + COMMAND_WORD + " 1 birthday clique";

    public static final String MESSAGE_SUCCESS = "Untagged Task at Index: %1$d\n%2$s";

    public final int targetIndex;
    public final UniqueTagList tags;
    
    public UntagCommand(int targetIndex, String tagNames) throws IllegalValueException {

        this.targetIndex = targetIndex;
        
        if (tagNames.isEmpty()) {
            throw new IllegalValueException("Tag Names cannot be empty");
        }
        
        tags = new UniqueTagList();
        for (String tagName : tagNames.trim().split(" ")) {
            tags.add(new Tag(tagName));
        }
        
    }
    
    /**
     * Executes the untag command.
     * 
     * If the index is invalid or if the specified task cannot be found, returns the 
     * relevant message to inform the user.
     */
    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToUntag = lastShownList.get(targetIndex - 1);

        try {
            model.deleteTaskTags(taskToUntag, tags);
            
            model.refreshCurrentFilteredTaskList();
            model.updateTodayListToShowAll();
            model.updateWeekListToShowAll();
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be found";
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, targetIndex, taskToUntag));
    }
    
    
    
}
