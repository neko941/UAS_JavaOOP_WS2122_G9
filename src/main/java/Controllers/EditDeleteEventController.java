package Controllers;

import Models.*;
import com.calendarfx.model.Entry;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.Desktop;

import static Controllers.EventController.*;
import static ExternalConnections.DBUtilities.*;
import static java.lang.Integer.parseInt;

public class EditDeleteEventController extends Application {
    private int selectedId;
    @FXML private ChoiceBox eventChoice;
    @FXML private TextField eventName;
    @FXML private DatePicker eventDate;
    @FXML private TextField eventTime;
    @FXML private TextField eventMinutes;
    @FXML private TextField eventDuration;
    @FXML private TextField eventStreet;
    @FXML private TextField eventHouseNr;
    @FXML private TextField eventZipCode;
    @FXML private TextField eventCity;
    @FXML private TextField eventCountry;
    @FXML private TextField participants;
    @FXML private ChoiceBox priority;
    @FXML private ChoiceBox reminder;
    @FXML private Button createButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    @FXML private Button attachmentsButton;
    @FXML private Button openButton;
    @FXML private Label errorLabel;

    private User currentUser;
    FileChooser fileChooser = new FileChooser();
    ArrayList<File> attachment = new ArrayList<File>();

    /**
     * Sets the current logged-in user which will be used to fetch and create events.
     *
     * @param: currentUser: object of type User
     *
     * @return: void
     */
    public void setCurrentUser(User currentUser){ this.currentUser = currentUser; }

    /**
     * Loads the View/Edit event UI,initial events and loads events on the dropdown list for selection.
     *
     * @param: primaryStage - Stage on which the page will be rendered
     * @return: void
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtilities();
        URL resourceUrl = getClass().getResource("/UI/EditDeleteEventUI.fxml");
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        loader.setController(this);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        ArrayList<Event> myEvents = fetchAllEventsFromUser(currentUser);
        for (int i = 0; i < myEvents.size(); i++) {
            Event thisEvent = myEvents.get(i);
            String id = String.valueOf(thisEvent.getEventID());
            String title = thisEvent.getEventName();
            String dateTime = LocalDateTime.of(thisEvent.getDate(), thisEvent.getTime()).toString();
            eventChoice.getItems().add(id + ": " + title + " " + dateTime);
        }
        createButton.setDisable(true);
        deleteButton.setDisable(true);
        errorLabel.setText("");
        attachmentsButton.setDisable(true);
        openButton.setDisable(true);
        stage.show();
    }

    /**
     * Loads the data from a selected event into fields for viewing/editing.
     *
     * @return: void
     */
    @FXML
    public void selectEventOnAction() {
        int eventId = parseInt(eventChoice.getValue().toString().split(":")[0]);
        Event selectedEvent = fetchEventsFromID(eventId);
        eventName.setText(selectedEvent.getEventName());
        eventDate.setValue(selectedEvent.getDate());
        eventTime.setText(selectedEvent.getTime().toString().split(":")[0]);
        eventMinutes.setText(selectedEvent.getTime().toString().split(":")[1]);
        eventDuration.setText(Integer.toString(selectedEvent.getDuration()));
        priority.setValue(selectedEvent.getPriority());
        reminder.setValue(selectedEvent.getReminder());
        participants.setText(StringUtils.join(selectedEvent.getEmails(), ","));
        this.selectedId = eventId;
        /*
        ArrayList<User> eventParticipants = fetchParticipants(eventId);
        String userIds = "";
        for (int i = 0; i< eventParticipants.size(); i++){
            String email = eventParticipants.get(i).getEmail();

            userIds += email + "; ";
        }
        participants.setText(userIds);*/

        Location myLocation = selectedEvent.getLocation();
        eventStreet.setText(myLocation.getStreet());
        eventHouseNr.setText(myLocation.getStreetNumber());
        eventZipCode.setText(myLocation.getZip());
        eventCity.setText(myLocation.getCity());
        eventCountry.setText(myLocation.getCountry());
        attachment = selectedEvent.getAttachments();
        createButton.setDisable(false);
        deleteButton.setDisable(false);
        attachmentsButton.setDisable(false);
        if(attachment.size() != 0) {
            openButton.setDisable(false);
        }
    }

    /**
     * Saves the updated Event information by creating an event object and calling the function from the EventController.
     *
     * @return: void
     */
    @FXML
    public void EditEventOnAction() {
        Priority selectedPriority = mapPriority(priority.getValue().toString());
        Reminder selectedReminder = mapReminder(reminder.getValue().toString());
        String[] emails = participants.getText().replaceAll("\\s","").split(",");
        parseInt(eventDuration.getText());
        ArrayList<User> mappedParticipants = new ArrayList<User>();
        if(!eventName.getText().trim().isEmpty() &&
                !eventTime.getText().trim().isEmpty() &&
                !eventMinutes.getText().trim().isEmpty() &&
                !eventDuration.getText().trim().isEmpty() &&
                !eventStreet.getText().trim().isEmpty() &&
                !eventHouseNr.getText().trim().isEmpty() &&
                !eventZipCode.getText().trim().isEmpty() &&
                !eventCity.getText().trim().isEmpty() &&
                !eventCountry.getText().trim().isEmpty() ) {

            errorLabel.setText("");
            mappedParticipants.add(currentUser);
            for (int i = 0; i < emails.length; i++) {
                User myUser = fetchUser(emails[i]);
                mappedParticipants.add(myUser);
            }

            Event myEvent = fetchEventsFromID(selectedId);

            EditEvent(myEvent,
                    eventName.getText(),
                    eventDate.getValue(),
                    LocalTime.of(parseInt(eventTime.getText()),
                            parseInt(eventMinutes.getText())),
                    parseInt(eventDuration.getText()),
                    new Location(eventStreet.getText().replaceAll("\\s", ""),
                            eventHouseNr.getText().replaceAll("\\s", ""),
                            eventZipCode.getText().replaceAll("\\s", ""),
                            eventCity.getText().replaceAll("\\s", ""),
                            eventCountry.getText().replaceAll("\\s", ""),
                            "",
                            ""),
                    mappedParticipants,
                    emails,
                    attachment,
                    selectedReminder,
                    selectedPriority);
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.close();
        }
        else{
            errorLabel.setText("Missing Data!");
        }
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
     * Opens up the attachments from the currently selected event.
     *
     * @return: void
     */
    @FXML
    public void openButtonOnAction() throws IOException {
        Desktop myDesktop = Desktop.getDesktop();
        for(int i = 0; i < attachment.size(); i++){
            myDesktop.open(attachment.get(i));
        }
    }

    /**
     * Deletes the currently selected event.
     *
     * @return: void
     */
    @FXML
    public void DeleteButtonOnAction() {
        Event myEvent = fetchEventsFromID(selectedId);
        DeleteEvent(currentUser, myEvent);
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the current page.
     *
     * @return: void
     */
    @FXML
    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * maps the selection to a Priority object.
     *
     * @param: the string currently selected on the ChoiceBox
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
     * @param: the string currently selected on the ChoiceBox
     * @return: the equivalent Reminder object of the selection string
     */
    public Reminder mapReminder(String selection) {
        switch(priority.getValue().toString()){
            case "1 week":
                return Reminder.ONE_WEEK;
            case "3 days":
                return Reminder.THREE_DAYS;
            case "1 hour":
                return Reminder.ONE_HOUR;
            case "10 minutes":
                return Reminder.TEN_MINUTES;
        }
        return Reminder.TEN_MINUTES;
    }
}
