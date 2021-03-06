package seedu.todo.model.task;

import seedu.todo.commons.exceptions.IllegalValueException;

//@@author A0121643R
/**
 * Represents a Task's priority in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidpriority(String)}
 */
public class Priority {

    public static final String PRIORITY_RULE = "Task priority should be high, mid or low";
    public static final String PRIORITY_REGEX = "(high|mid|low)";
    public static final String DEFAULT_PRIORITY = "low";
    public static final String LOW = "low";
    public static final String MID = "mid";
    public static final String HIGH = "high";

    public final String priorityLevel;

    /**
     * Validates given priority.
     *
     * @throws IllegalValueException if given priority string is invalid.
     */
    public Priority(String priority) throws IllegalValueException {
        assert priority != null;
        String tempPriority = priority.trim();
        if (!isValidpriority(tempPriority)) {
            throw new IllegalValueException(PRIORITY_RULE);
        }
        this.priorityLevel = tempPriority;
    }

    /**
     * Returns true if a given string is a valid task priority.
     */
    public static boolean isValidpriority(String test) {
        return test.matches(PRIORITY_REGEX);
    }
    
    //@@author A0121643R-unused
    /**
     * higher priority is smaller so that can be shown in front
     */

	public int compareTo(Priority other) {
		if(! this.priorityLevel.equals(other.priorityLevel)) {
			if (this.priorityLevel.equals(HIGH)) {
				return -1;
			} else if (this.priorityLevel.equals(LOW)) {
				return 1;
			} else if (this.priorityLevel.equals(MID) && other.priorityLevel.equals(HIGH)) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}
	//@@author
	
	//@@author A0121643R

    @Override
    public String toString() {
        return priorityLevel;
    }

    @Override
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
