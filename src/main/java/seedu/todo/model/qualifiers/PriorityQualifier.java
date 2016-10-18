package seedu.todo.model.qualifiers;

import seedu.todo.model.task.ReadOnlyTask;

public class PriorityQualifier implements Qualifier {
    private String priority;

    public PriorityQualifier(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean run(ReadOnlyTask task) {
        return task.getPriority().equals(priority);

    }

    @Override
    public String toString() {
        return "priority=" + priority;
    }

}
