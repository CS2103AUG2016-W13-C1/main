package seedu.todo.testutil;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.model.DoDoBird;
import seedu.todo.model.task.*;
import seedu.todo.model.task.Recurrence.Frequency;
//@@author A0093896H
/**
 * class to generate typical tasks
 */
public class TypicalTestTasks {

    public static TestTask buyGroceries, buyMilk, buyRice, buyChilli, buyNoodles, buyCheese;

    public TypicalTestTasks() {
        try {
            buyGroceries =  new TaskBuilder().withName("Buy Groceries").withByDate("12/12/2016")
                    .withOnDate("12/10/2016").withDetail("fish").withCompletion(true)
                    .withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
            buyMilk =  new TaskBuilder().withName("Buy Milk").withByDate("12/12/2016")
                    .withOnDate("20/10/2016").withDetail("Marigold").withCompletion(true)
                    .withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
            buyRice =  new TaskBuilder().withName("Buy Rice").withByDate("12/12/2016")
                    .withOnDate("30/10/2016").withDetail("Thai Rice").withCompletion(true).
                    withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
            buyChilli =  new TaskBuilder().withName("Buy Chilli").withByDate("12/12/2016")
                    .withOnDate("12/11/2016").withDetail("Red").withCompletion(true)
                    .withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
            buyNoodles = new TaskBuilder().withName("Buy Noodles").withByDate("12/12/2016")
                    .withOnDate("12/11/2016").withDetail("Red").withCompletion(true)
                    .withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
            buyCheese = new TaskBuilder().withName("Buy Cheese").withByDate("12/12/2016")
                    .withOnDate("12/11/2016").withDetail("Red").withCompletion(true)
                    .withRecurrence(Frequency.NONE).withTags("urgent").withPriority("mid").build();
            
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadToDoListWithSampleData(DoDoBird ab) {

        try {
            ab.addTask(new Task(buyGroceries));
            ab.addTask(new Task(buyChilli));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{buyGroceries, buyChilli, buyMilk, buyCheese};
    }
    
    public TestTask[] getTypicalTasksReverse() {
        return new TestTask[]{buyCheese, buyMilk, buyChilli, buyGroceries};
    }
    
    public TestTask[] getEmptyTaskList() {
        return new TestTask[]{};
    }

    public DoDoBird getTypicalToDoList(){
        DoDoBird ab = new DoDoBird();
        loadToDoListWithSampleData(ab);
        return ab;
    }
}
