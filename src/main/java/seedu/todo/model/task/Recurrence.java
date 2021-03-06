package seedu.todo.model.task;

import seedu.todo.commons.exceptions.IllegalValueException;
//@@author A0093896H
/**
 * Represents a Task's name in the to do list.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Recurrence {
    
    public enum Frequency {
        NONE,
        YEAR,
        MONTH,
        WEEK,
        DAY
    }
    
    private Frequency freq;
    
    public Recurrence(Frequency freq) throws IllegalValueException {
        assert freq != null;
        this.freq = freq;
    }
    
    public Frequency getFreq() {
        return this.freq;
    }
    
    public void setFreq(Frequency freq) {
        this.freq = freq;
    }
        
    public boolean isRecurring() {
        return this.freq != Frequency.NONE;
    }
    
    @Override
    public String toString() {
        switch(this.freq) {
        case YEAR :
            return "YEAR";
        case MONTH :
            return "MONTH";
        case WEEK:
            return "WEEK";
        case DAY :
            return "DAY";
        default :
            return "NONE";
        }
    }
    
    /**
     * Update the dates for a task based on the recurrence of the task.
     * Will mark the task as undone as well.
     */
    public void updateTaskDate(Task task){
        
        task.getCompletion().setCompletion(false);
        
        switch(this.freq) {
        case YEAR :
            if (task.getOnDate().getDate() != null) {
                task.getOnDate().setDate(task.getOnDate().getDate().plusYears(1));
            }
            if (task.getByDate().getDate() != null) {
                task.getByDate().setDate(task.getByDate().getDate().plusYears(1));
            }
            break;
        case MONTH :
            if (task.getOnDate().getDate() != null) {
                task.getOnDate().setDate(task.getOnDate().getDate().plusMonths(1));
            }
            if (task.getByDate().getDate() != null) {
                task.getByDate().setDate(task.getByDate().getDate().plusMonths(1));
            }
            break;
        case WEEK:
            if (task.getOnDate().getDate() != null) {
                task.getOnDate().setDate(task.getOnDate().getDate().plusWeeks(1));
            }
            if (task.getByDate().getDate() != null) {
                task.getByDate().setDate(task.getByDate().getDate().plusWeeks(1));
            }
            break;
        case DAY :
            if (task.getOnDate().getDate() != null) {
                task.getOnDate().setDate(task.getOnDate().getDate().plusDays(1));
            }
            if (task.getByDate().getDate() != null) {
                task.getByDate().setDate(task.getByDate().getDate().plusDays(1));
            }
            break;
        default :
            //Default value is NONE - do not need to update
        }
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Recurrence // instanceof handles nulls
                && this.freq.equals(((Recurrence) other).freq)); // state check
    }
    
}
