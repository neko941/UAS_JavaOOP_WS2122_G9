package Controllers;

import Models.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;



public abstract class EventUIController extends Application {
    @FXML protected TextField eventName;
    @FXML protected DatePicker eventDate;
    @FXML protected TextField eventTime;
    @FXML protected TextField eventMinutes;
    @FXML protected TextField eventDuration;
    @FXML protected TextField eventStreet;
    @FXML protected TextField eventHouseNr;
    @FXML protected TextField eventZipCode;
    @FXML protected TextField eventCity;
    @FXML protected TextField eventCountry;
    @FXML protected TextField participants;
    @FXML protected ChoiceBox priority;
    @FXML protected ChoiceBox reminder;
    @FXML protected Button createButton;
    @FXML protected Button cancelButton;
    @FXML protected Button attachmentsButton;
    @FXML protected Label userlable;
    @FXML protected Label errorLabel;
    FileChooser fileChooser = new FileChooser();
    public User currentUser;
    ArrayList<File> attachment = new ArrayList<File>();
    @Override
    public abstract void start(Stage primaryStage) throws Exception;

    /**
     * Sets the current logged-in user which will be used to fetch and create events.
     *
     * @param currentUser: object of type User
     *
     * @return: void
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Closes the current page.
     *
     * @return: void
     */
    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Adds a new attachment to class attachments array, to be passed down to the controller and subsequently to the DB upon saving the changes.
     *
     * @return: void
     */
    @FXML
    public void attachmentButtonOnAction() {
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        this.attachment.add(file);
    }

    /**
     * maps the selection to a Priority object.
     *
     * @param selection: the string currently selected on the ChoiceBox
     * @return: the equivalent Priority object of the selection string
     */
    public Priority mapPriority(String selection) {
        switch(priority.getValue().toString()){
            case "LOW":
                return Priority.LOW;
            case "MEDIUM":
                return Priority.MEDIUM;
            case "HIGH":
                return Priority.HIGH;
        }
        return Priority.HIGH;
    }

    /**
     * maps the selection to a Reminder object.
     *
     * @param selection the string currently selected on the ChoiceBox
     * @return: the equivalent Reminder object of the selection string
     */
    public Reminder mapReminder(String selection) {
        switch(reminder.getValue().toString()){
            case "1 week":
                return Reminder.ONE_WEEK;
            case "3 days":
                return Reminder.THREE_DAYS;
            case "1 hour":
                return Reminder.ONE_HOUR;
            case "10 minutes":
                return Reminder.TEN_MINUTES;
            case "No reminder":
                return Reminder.NO_REMINDER;
        }
        return Reminder.NO_REMINDER;
    }

}

