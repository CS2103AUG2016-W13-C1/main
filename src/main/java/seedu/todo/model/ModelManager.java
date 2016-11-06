package seedu.todo.model;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.todo.commons.core.ComponentManager;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.core.UnmodifiableObservableList;
import seedu.todo.commons.events.model.ToDoListChangedEvent;
import seedu.todo.commons.events.ui.TagPanelSelectionEvent;
import seedu.todo.logic.commands.SearchCommand.SearchCompletedOption;
import seedu.todo.model.expressions.Expression;
import seedu.todo.model.expressions.PredicateExpression;
import seedu.todo.model.qualifiers.*;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.tag.UniqueTagList;
import seedu.todo.model.task.Priority;
import seedu.todo.model.task.ReadOnlyTask;
import seedu.todo.model.task.Task;
import seedu.todo.model.task.UniqueTaskList;
import seedu.todo.model.task.UniqueTaskList.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

/**
 * Represents the in-memory model of the to do list data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final DoDoBird dodobird;
    private final FilteredList<Task> filteredTasks; //for main task list
    private final FilteredList<Task> todayTasks; //for today summary list
    private final FilteredList<Task> weekTasks; //for weekly summary list
    private final FilteredList<Tag> tagList;
    private SortedList<Task> sortedTasks;
    
    //@@author A0138967J
    /**
     * Initializes a ModelManager with the given ToDoList
     * ToDoList and its variables should not be null
     */
    public ModelManager(ReadOnlyToDoList src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with to-do app: " + src + " and user prefs " + userPrefs);

        dodobird = new DoDoBird(src);
        filteredTasks = new FilteredList<>(dodobird.getTasks());
        todayTasks = new FilteredList<>(dodobird.getTasks());
        weekTasks = new FilteredList<>(dodobird.getTasks());
        tagList = new FilteredList<>(dodobird.getTags());
        sortedTasks = new SortedList<>(filteredTasks, Task.getTaskComparator());
        
        updateFilteredListToShowAllNotCompleted();
        updateTodayListToShowAll();
        updateWeekListToShowAll();
    }
    //@@author
    
    public ModelManager() {
        this(new DoDoBird(), new UserPrefs());
    }    
    
    
    @Override
    public ReadOnlyToDoList getToDoList() {
        return dodobird;
    }
    
    @Override
    public void resetData(ReadOnlyToDoList newData) {
        dodobird.resetData(newData);
        indicateToDoListChanged();
    }
    
    /** Raises an event to indicate the model has changed */
    public void indicateToDoListChanged() {
        raise(new ToDoListChangedEvent(dodobird));
    }
    
    @Override
    public boolean undo() {
        if (dodobird.undo()) {
            indicateToDoListChanged();
            return true;
        }
        return false;
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        dodobird.addTask(task);
        indicateToDoListChanged();
    }
    
    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        dodobird.deleteTask(target);
        indicateToDoListChanged();
    }
    
    //@@author A0093896H
    @Override
    public synchronized Task getTask(ReadOnlyTask target) {
        return dodobird.getTask(dodobird.getTaskIndex(target));
    }
    
    @Override
    public synchronized void updateTask(ReadOnlyTask oldTask, ReadOnlyTask newTask) throws TaskNotFoundException {
        dodobird.updateTask(oldTask, newTask);
        indicateToDoListChanged();
    }
    
    @Override
    public synchronized void addTaskTags(ReadOnlyTask oldTask, UniqueTagList newTagList) throws TaskNotFoundException {
        dodobird.addTaskTags(oldTask, newTagList);
        indicateToDoListChanged();
    }
    
    @Override
    public synchronized void deleteTaskTags(ReadOnlyTask oldTask, UniqueTagList newTagList) 
            throws TaskNotFoundException {
        dodobird.deleteTaskTags(oldTask, newTagList);
        indicateToDoListChanged();
    }
    
    //=========== Filtered Task List Accessors ===============================================================
    
    //@@author A0121643R
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return getSortedTaskList();
    }

    /**
     * Sorts filtered list based on bydate, priority, name
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getSortedTaskList() {
        sortedTasks = new SortedList<>(filteredTasks, Task.getTaskComparator());
        return new UnmodifiableObservableList<>(sortedTasks);
    }
    
    //@@author A0138967J
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTodayTaskList() {
        return new UnmodifiableObservableList<>(todayTasks);
    } 
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredWeekTaskList() {
        return new UnmodifiableObservableList<>(weekTasks);
    } 
    
    //@@author A0142421X
    public UnmodifiableObservableList<Tag> getUnmodifiableTagList() {
        return new UnmodifiableObservableList<>(tagList);
    } 
    
    //@@author A0138967J
    @Override
    public void updateTodayListToShowAll() {
        todayTasks.setPredicate(null);
        todayTasks.setPredicate((new PredicateExpression(new TodayDateQualifier(LocalDateTime.now())))::satisfies);
    }
    @Override
    public void updateWeekListToShowAll() {
        weekTasks.setPredicate(null);
        weekTasks.setPredicate((new PredicateExpression(new WeekDateQualifier(LocalDateTime.now())))::satisfies);
    }

    //@@author A0093896H
    @Override
    public void updateFilteredListToShowAll() {
        updateFilteredTaskList(new PredicateExpression(new CompletedQualifier(true))); //force change
        filteredTasks.setPredicate(null);
    }
    
    @Override
    public void updateFilteredListToShowAllCompleted(){
        updateFilteredTaskList(new PredicateExpression(new CompletedQualifier(true)));
    }
    
    @Override
    public void updateFilteredListToShowAllNotCompleted(){
        updateFilteredTaskList(new PredicateExpression(new CompletedQualifier(false)));
    }

    @Override
    public void updateFilteredTaskListByKeywords(Set<String> keywords, SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new KeywordQualifier(keywords, option)));
    }

    @Override
    public void updateFilteredTaskListByTag(String tagName, SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new TagQualifier(tagName, option)));
    }
    
    @Override
    public void updateFilteredTaskListOnDate(LocalDateTime datetime, boolean hasTimeField, 
            SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new OnDateQualifier(datetime, hasTimeField, option)));
    }
    
    @Override
    public void updateFilteredTaskListBeforeDate(LocalDateTime datetime, SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new BeforeDateQualifier(datetime, option)));
    }

    @Override
    public void updateFilteredTaskListAfterDate(LocalDateTime datetime, SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new AfterDateQualifier(datetime, option)));
    }
    
    @Override
    public void updateFilteredTaskListFromTillDate(LocalDateTime fromDateTime, LocalDateTime tillDateTime, 
            SearchCompletedOption option){
        updateFilteredTaskList(new PredicateExpression(new FromTillDateQualifier(fromDateTime, tillDateTime, option)));
    }
    
    @Override
    public void updateFilteredListToShowAllFloating(SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new FloatingQualifier(option)));
    }

    //@@author A0121643R
    @Override
    public void updateFilteredTaskListByPriority(Priority priority, SearchCompletedOption option) {
        updateFilteredTaskList(new PredicateExpression(new PriorityQualifier(priority, option)));   
    }

    //@@author A0138967J-unused
    @Override
    public void updateFilteredTaskListTodayDate(LocalDateTime datetime){
        updateFilteredTaskList(new PredicateExpression(new TodayDateQualifier(datetime)));
    }
    //@@author
    
    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    //@@author A0142421X
    @Subscribe
    private void handleTagPanelSelectionEvent(TagPanelSelectionEvent tpse) {
        this.updateFilteredTaskListByTag(tpse.tag.getName(), SearchCompletedOption.UNDONE);
    }
    
    
}
