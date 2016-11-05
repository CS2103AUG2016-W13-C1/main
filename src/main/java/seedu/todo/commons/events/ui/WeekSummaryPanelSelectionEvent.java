//@@author A0138967J
package seedu.todo.commons.events.ui;

import seedu.todo.commons.events.BaseEvent;

public class WeekSummaryPanelSelectionEvent extends BaseEvent {
    
    public WeekSummaryPanelSelectionEvent() {}
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
}
