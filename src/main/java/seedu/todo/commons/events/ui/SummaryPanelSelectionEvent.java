//@@author A0138967J
package seedu.todo.commons.events.ui;

import seedu.todo.commons.events.BaseEvent;

public class SummaryPanelSelectionEvent extends BaseEvent {
    
    public SummaryPanelSelectionEvent() {}
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
}
