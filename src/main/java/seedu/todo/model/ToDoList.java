package seedu.todo.model;

import javafx.collections.ObservableList;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.tag.UniqueTagList;
import seedu.todo.model.task.ReadOnlyTask;
import seedu.todo.model.task.Task;
import seedu.todo.model.task.UniqueTaskList;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class ToDoList implements ReadOnlyToDoList {

    private final Stack<UniqueTaskList> tasksHistory;
    private final Stack<UniqueTagList> tagsHistory;

    public ToDoList() {
        this(new UniqueTaskList(), new UniqueTagList());
    }

    /**
     * Tasks and Tags are copied into this ToDoList
     */
    public ToDoList(ReadOnlyToDoList toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Tasks and Tags are copied into this ToDoList
     */
    public ToDoList(UniqueTaskList tasks, UniqueTagList tags) {
        tasksHistory = new Stack<>();
        tagsHistory = new Stack<>();
        resetData(tasks.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlyToDoList getEmptyToDoList() {
        return new ToDoList();
    }

//// list overwrite operations

    public ObservableList<Task> getTasks() {
        return tasksHistory.peek().getInternalList();
    }
    
    public ObservableList<Tag> getTags() {
    	return tagsHistory.peek().getInternalList();
    }

    
    public void resetData(Collection<? extends ReadOnlyTask> newTasks, Collection<Tag> newTags) {
        setTasks(newTasks.stream().map(Task::new).collect(Collectors.toList()));
        setTags(newTags);
        
        for (Task t : this.getTasks()) {
            if (t.isRecurring() 
                    && (t.getOnDate().getDate().isBefore(LocalDate.now()) 
                    || t.getByDate().getDate().isBefore(LocalDate.now()))) {
                t.getRecurrence().updateTaskDate(t);
            }
        }
    }

    public void resetData(ReadOnlyToDoList newData) {
        resetData(newData.getTaskList(), newData.getTagList());
    }
    

    public void setTasks(List<Task> tasks) {
        if (this.tasksHistory.isEmpty()) {
            UniqueTaskList topList = this.createNewTaskList(tasks);
            this.tasksHistory.push(topList);
        } else {
            UniqueTaskList topList = this.tasksHistory.pop();
            UniqueTaskList oldList = this.createNewTaskList(topList.getInternalList());
            
            this.tasksHistory.push(oldList);
            topList.getInternalList().setAll(tasks);
            this.tasksHistory.push(topList);
        }
        
    }

    public void setTags(Collection<Tag> tags) {
        UniqueTagList newList = this.createNewTagList(tags);
        this.tagsHistory.push(newList);
    }


//// task-level operations

    /**
     * Adds a task to the address book.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        UniqueTaskList topList = this.tasksHistory.pop();
        UniqueTaskList oldList = this.createNewTaskList(topList.getInternalList());
        this.tasksHistory.push(oldList);
        this.tasksHistory.push(topList);
        topList.add(p);
        
    }

    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    public void syncTagsWithMasterList(Task task) {
        final UniqueTagList taskTags = task.getTags();
        UniqueTagList newList = this.createNewTagList(this.tagsHistory.peek().getInternalList());
        newList.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : newList) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of task tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : taskTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        task.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        UniqueTaskList topList = this.tasksHistory.pop();
        UniqueTaskList oldList = this.createNewTaskList(topList.getInternalList());
        this.tasksHistory.push(oldList);
        this.tasksHistory.push(topList);
        if (topList.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    /**
     * Pop the top most UniqueTaskList.
     * TODO : Does not handle tags as of yet
     */
    public boolean undo() {
        if (this.tasksHistory.size() > 1) {
            UniqueTaskList topList = this.tasksHistory.pop();
            UniqueTaskList oldList = this.tasksHistory.pop();
            topList.getInternalList().setAll(oldList.getInternalList());
            this.tasksHistory.push(topList);
            return true;
        }
        return false;
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        UniqueTagList topList = this.tagsHistory.pop();
        UniqueTagList oldList = this.createNewTagList(topList.getInternalList());
        this.tagsHistory.push(oldList);
        this.tagsHistory.push(topList);
        topList.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return tasksHistory.peek().getInternalList().size() + " tasks, " 
                + tagsHistory.peek().getInternalList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return Collections.unmodifiableList(tasksHistory.peek().getInternalList());
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tagsHistory.peek().getInternalList());
    }
    
    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasksHistory.peek();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tagsHistory.peek();
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ToDoList // instanceof handles nulls
                && this.tasksHistory.peek().equals(((ToDoList) other).tasksHistory.peek())
                && this.tagsHistory.peek().equals(((ToDoList) other).tagsHistory.peek()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasksHistory.peek(), tagsHistory.peek());
    }
    
    private UniqueTaskList createNewTaskList(Collection<Task> old) {
        UniqueTaskList newList = new UniqueTaskList();

        for (Task t : old) {
            try {
                newList.add(new Task(t));
            } catch (UniqueTaskList.DuplicateTaskException e) {}
        }
        return newList;
    }
    
    private UniqueTagList createNewTagList(Collection<Tag> old) {
        UniqueTagList newList = new UniqueTagList();
        
        for (Tag t : old) {
            try {
                newList.add(t);
            } catch (UniqueTagList.DuplicateTagException e) {}
        }
        return newList;
    }
    
}
