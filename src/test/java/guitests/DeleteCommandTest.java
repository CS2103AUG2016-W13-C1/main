package guitests;

import org.junit.Test;

import seedu.todo.testutil.TestTask;
import seedu.todo.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.todo.logic.commands.DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS;

public class DeleteCommandTest extends ToDoListGuiTest {
	//@@author A0093896H
    @Test
    public void deleteCommandTest() {
               
        //delete the first in the list
        TestTask[] currentList = td.getTypicalTasks();
        for (TestTask t : currentList) {
            commandBox.runCommand(t.getAddCommand());
        }
        int targetIndex = 1;
        TestTask[] currentRevList = td.getTypicalTasksReverse();
        assertDeleteSuccess(targetIndex, currentRevList);

        //delete the last in the list
        currentRevList = TestUtil.removeTaskFromList(currentRevList, targetIndex);
        targetIndex = currentRevList.length;
        assertDeleteSuccess(targetIndex, currentRevList);

        //delete from the middle of the list
        currentRevList = TestUtil.removeTaskFromList(currentRevList, targetIndex);
        targetIndex = currentRevList.length / 2;
        assertDeleteSuccess(targetIndex, currentRevList);

        //invalid index
        commandBox.runCommand("delete " + currentRevList.length + 1);
        assertResultMessage("The task index provided is invalid");

    }
    //@@author
    
    /**
     * Runs the delete command to delete the task at specified index and confirms the result is correct.
     * 
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        TestTask taskToDelete = currentList[targetIndexOneIndexed - 1]; //-1 because array uses zero indexing
        TestTask[] expectedRemainder = TestUtil.removeTaskFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("delete " + targetIndexOneIndexed);
        
        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_TASK_SUCCESS, targetIndexOneIndexed, taskToDelete));
    
    }
}
