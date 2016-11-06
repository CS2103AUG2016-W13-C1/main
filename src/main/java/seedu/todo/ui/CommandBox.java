package seedu.todo.ui;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.todo.commons.events.ui.SummaryPanelSelectionEvent;
import seedu.todo.commons.events.ui.TagPanelSelectionEvent;
import seedu.todo.commons.events.ui.WeekSummaryPanelSelectionEvent;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.logic.Logic;
import seedu.todo.logic.commands.*;

import java.util.Stack;
import java.util.logging.Logger;

public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    
    //@@author A0093896H
    private Stack<String> commandHistory = new Stack<>();
    private Stack<String> commandFuture = new Stack<>();
    //@@author
    
    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
    private String previousCommandTest;

    private Logic logic;

    @FXML
    private TextField commandTextField;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
            ResultDisplay resultDisplay, Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
        commandBox.configure(resultDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

    public void configure(ResultDisplay resultDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
        this.logic = logic;
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }


    @FXML
    private void handleCommandInputChanged() {
        //Take a copy of the command text
        previousCommandTest = commandTextField.getText();
        commandHistory.push(previousCommandTest);

        /* We assume the command is correct. If it is incorrect, the command box will be changed accordingly
         * in the event handling code {@link #handleIncorrectCommandAttempted}
         */
        setStyleToIndicateCorrectCommand();
        CommandResult mostRecentResult = logic.execute(previousCommandTest);
        resultDisplay.postMessage(mostRecentResult.feedbackToUser);
        logger.info("Result: " + mostRecentResult.feedbackToUser);
    }
    
    //@@author A0093896H
    @FXML
    private void handleKeyPressed(KeyEvent e) {
        if (e.getCode().toString().equals("UP") && !commandHistory.isEmpty()) {
            String c = commandHistory.pop();
            commandFuture.push(c);
            commandTextField.setText(c);
        }
        if (e.getCode().toString().equals("DOWN") && !commandFuture.isEmpty()) {
            String c = commandFuture.pop();
            commandHistory.push(c);
            commandTextField.setText(c);
        }
    }
    //@@author

    /**
     * Sets the command box style to indicate a correct command.
     */
    private void setStyleToIndicateCorrectCommand() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.setText("");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Invalid command: " + previousCommandTest));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

    @Subscribe
    private void handleTagPanelSelectionEvent(TagPanelSelectionEvent tpse){
        resultDisplay.postMessage("Displaying list of tasks with tag: " + tpse.tag.getName());
    }

    @Subscribe
    private void handleSummaryPanelSelectionEvent(SummaryPanelSelectionEvent spse){
        resultDisplay.postMessage("Displaying list of tasks today");
    }
    @Subscribe
    private void handleWeekSummaryPanelSelectionEvent(WeekSummaryPanelSelectionEvent wpse){
        resultDisplay.postMessage("Displaying list of tasks for next 7 days");
    }
    
    
    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandTest);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }

}
