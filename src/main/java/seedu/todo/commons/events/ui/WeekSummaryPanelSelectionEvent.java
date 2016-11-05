//@@author A0138967J
package seedu.todo.commons.events.ui;

import seedu.todo.commons.events.BaseEvent;
import seedu.todo.model.task.ReadOnlyTask;

public class WeekSummaryPanelSelectionEvent extends BaseEvent {
    
    ReadOnlyTask task;
    
    public WeekSummaryPanelSelectionEvent(ReadOnlyTask task) {
        this.task = task;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
}
