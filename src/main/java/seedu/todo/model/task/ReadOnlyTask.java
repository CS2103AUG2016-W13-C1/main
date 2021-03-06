package seedu.todo.model.task;

import seedu.todo.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the todo application.
 * Implementations should guarantee: |name is not null, field values are validated.
 */
public interface ReadOnlyTask {
    
    Name getName();
    Detail getDetail();
    TaskDate getOnDate();
    TaskDate getByDate();
    Priority getPriority();
    Completion getCompletion();
    Recurrence getRecurrence();
    boolean isRecurring();
    
    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    //@@author A0093896H
    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || ((other != null) // this is first to avoid NPE below
                && other.getName().equals(this.getName())
                && other.getDetail().equals(this.getDetail())
                && other.getByDate().equals(this.getByDate())
                && other.getOnDate().equals(this.getOnDate())
                && other.getPriority().equals(this.getPriority())
                && other.getRecurrence().equals(this.getRecurrence()));
    }

    /**
     * Formats the task as text, showing all task information.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName() + "\n")
                .append("Details: ")
                .append(getDetail() + "\n")
                .append("Start: ")
                .append(getOnDate() + "\n")
                .append("End: ")
                .append(getByDate() + "\n")
                .append("Priority: ")
                .append(getPriority() + "\n")
                .append("Recurrence: ")
                .append(getRecurrence() + "\n");
        
        return builder.toString();
    }
    //@@author
    
    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
