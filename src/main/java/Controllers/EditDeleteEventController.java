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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static Controllers.EventController.*;
import static ExternalConnections.DBUtilities.*;
import static java.lang.Integer.parseInt;

public class EditDeleteEventController extends Application {
    private int selectedId;
    @FXML private ChoiceBox eventChoice;
    @FXML private TextField eventName;
    @FXML private DatePicker eventDate;
    @FXML private TextField eventTime;
    @FXML private TextField eventDuration;
    @FXML private TextField eventLocation;
    @FXML private TextField participants;
    @FXML private ChoiceBox priority;
    @FXML private ChoiceBox reminder;
    @FXML private Button createButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    private User currentUser;

    public void setCurrentUser(User currentUser){ this.currentUser = currentUser; }

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
        stage.show();
    }

    @FXML
    public void selectEventOnAction(ActionEvent event) {
        int eventId = parseInt(eventChoice.getValue().toString().split(":")[0]);
        Event selectedEvent = fetchEventsFromID(eventId);
        System.out.println(selectedEvent.getLocation());
        eventName.setText(selectedEvent.getEventName());
        eventDate.setValue(selectedEvent.getDate());
        eventTime.setText(selectedEvent.getTime().toString());
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
        eventLocation.setText(myLocation.getStreet() + ", " +  myLocation.getStreetNumber() + ", " +  myLocation.getZip() + ", " +  myLocation.getCity() + ", " +  myLocation.getCountry());


    }

    @FXML
    public void EditEventOnAction(ActionEvent event) {
        Priority selectedPriority = mapPriority(priority.getValue().toString());
        Reminder selectedReminder = mapReminder(reminder.getValue().toString());
        String[] emails = participants.getText().split(",");
        LocalTime.of(parseInt(eventTime.getText().split(":")[0]),
                parseInt(eventTime.getText().split(":")[1]));
        parseInt(eventDuration.getText());
        ArrayList<User> mappedParticipants = new ArrayList<User>();
        mappedParticipants.add(currentUser);
        for (int i = 0; i < emails.length; i++){
            User myUser = fetchUser(emails[i]);
            mappedParticipants.add(myUser);
        }

        Event myEvent = fetchEventsFromID(selectedId);
        String[] locationData = eventLocation.getText().split(",");

        EditEvent(myEvent,
                eventName.getText(),
                eventDate.getValue(),
                LocalTime.of(parseInt(eventTime.getText().split(":")[0]),
                        parseInt(eventTime.getText().split(":")[1])),
                parseInt(eventDuration.getText()),
                new Location(locationData[0].replaceAll("\\s",""),
                        parseInt(locationData[1].replaceAll("\\s","")),
                        locationData[2].replaceAll("\\s",""),
                        locationData[3].replaceAll("\\s",""),
                        locationData[4].replaceAll("\\s",""),
                        0,
                        0),
                mappedParticipants,
                emails,
                null,
                selectedReminder,
                selectedPriority);
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();


    }

    @FXML
    public void DeleteButtonOnAction(ActionEvent event) {
        Event myEvent = fetchEventsFromID(selectedId);
        DeleteEvent(myEvent);
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


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
