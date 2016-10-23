package seedu.todo.model.task;

import seedu.todo.commons.exceptions.IllegalValueException;



/**
 * Represents priority level of a task in the tasklist
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Priority {
	public static final String MESSAGE_NAME_CONSTRAINTS = "Priority should be high/mid/low only.";
    
	public static final String HIGH = "high";
	public static final String MID = "mid";
	public static final String LOW = "low";
    
	public final String priorityLevel;
	
	public Priority() {
		this.priorityLevel = LOW;
	}
    
    public Priority(String priority) throws IllegalValueException {
    	assert priority != null;
    	priority = priority.trim().toLowerCase();
    	if (!isPriorityLevel(priority)) {
    		throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
    		}
    	this.priorityLevel = priority;
    }
    
    public static boolean isPriorityLevel(String test) {
    	test = test.toLowerCase();
        return (test.equals(HIGH) || test.equals(MID) || test.equals(LOW));
    }
    
    public String toString() {
        return priorityLevel;
    }

    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && this.priorityLevel.equals(((Priority) other).priorityLevel)); // state check
    }

    @Override
    public int hashCode() {
        return priorityLevel.hashCode();
    }



}