package seedu.todo.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import seedu.todo.model.task.Priority;
import seedu.todo.model.task.ReadOnlyTask;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Text name;
    @FXML
    private Label id;
    @FXML
    private Text details;
    @FXML
    private Text recurrence;
    @FXML
    private Text onDate;
    @FXML
    private Text byDate;
    @FXML
    private Text tags;
    @FXML
    private Circle priorityLevel;

    private ReadOnlyTask task;
    private int displayedIndex;

    public static TaskCard load(ReadOnlyTask task, int displayedIndex){
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }
    
    //@@author A0121643R
    @FXML
    public void initialize() {
        
        id.setText(displayedIndex + ". ");
        name.setText(task.getName().fullName);
        details.setText(task.getDetail().value);
        
        initOnDate();
        initByDate();
        initRecurrence();
        initPriority();
        tags.setText(task.tagsString());
        
        if(checkOverdue(task)) {
        	CardStyler.styleForOverdue(name, details, onDate, byDate, tags, recurrence);
        } 
        
        if(task.getCompletion().isCompleted()) {
            CardStyler.styleForCompletion(name, details, onDate, byDate, tags, recurrence, priorityLevel);
        }
        
    }
    
    //@@author 
    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    //@@author A0093896H
    private void initOnDate() {
        if (task.getOnDate().getDate() != null) {
            onDate.setText("Start: " 
            			+ PrettifyDate.prettifyDate(task.getOnDate().getDate()) 
                        + " @ " + task.getOnDate().getTime() + " hrs");
        } else {
            onDate.setText("");
        }
    }
    
    private void initByDate() {
        if (task.getByDate().getDate() != null) {
            byDate.setText("End: " 
            			+ PrettifyDate.prettifyDate(task.getByDate().getDate()) 
                        + " @ " + task.getByDate().getTime() + " hrs");
        } else {
            byDate.setText("");
        }
    }
    
    private void initRecurrence() {
        if (task.isRecurring()) {
            recurrence.setText("Every: " + task.getRecurrence().toString());
        } else {
            recurrence.setText("");
        }
    }
    
    
    //@@author A0121643R
    private void initPriority() {
        if (task.getPriority().toString().equals(Priority.LOW)) {
            priorityLevel.setFill(Color.web("#b2ff59"));
            priorityLevel.setStroke(Color.LIMEGREEN);
        } else if (task.getPriority().toString().equals(Priority.MID)) {
            priorityLevel.setFill(Color.web("#fff59d"));
            priorityLevel.setStroke(Color.web("#ffff00"));
        } else {
            priorityLevel.setFill(Color.web("#ef5350"));
            priorityLevel.setStroke(Color.web("#c62828"));
        }
    }
    
    /**
     * return whether a task is overdue
     */
    private boolean checkOverdue(ReadOnlyTask task) {
    	return task.getByDate().getDate() != null && task.getByDate().getTime() != null 
    			&& task.getByDate().getDate().atTime(task.getByDate().getTime())
    			.isBefore(LocalDateTime.now());
    }
    
}
