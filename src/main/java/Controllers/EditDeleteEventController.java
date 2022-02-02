package Controllers;

import Models.Event;
import Models.Location;
import Models.Priority;
import Models.User;
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

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static ExternalConnections.DBUtilities.*;
import static java.lang.Integer.parseInt;

public class EditDeleteEventController extends Application {
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
    @FXML private Button cancelButton;
    private User currentUser;

    public void setCurrentUser(User currentUser){ this.currentUser = currentUser; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtilities();
        URL resourceUrl = getClass().getResource("/UI/EditDeleteEventUI.fxml");
        Parent parent = FXMLLoader.load(resourceUrl);
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @FXML
    public void LoadEventsOnAction(ActionEvent event) {
        ArrayList<Event> myEvents = fetchAllEventsFromUser(currentUser);
        for (int i = 0; i < myEvents.size(); i++) {
            Event thisEvent = myEvents.get(i);
            String id = String.valueOf(thisEvent.getEventID());
            String title = thisEvent.getEventName();
            String dateTime = LocalDateTime.of(thisEvent.getDate(), thisEvent.getTime()).toString();
            eventChoice.getItems().add(id + ": " + title + " " + dateTime);
        }
    }

    @FXML
    public void selectEventOnAction(ActionEvent event) {
        System.out.println("CHamou");
        int eventId = parseInt(eventChoice.getValue().toString().split(":")[0]);
        Event selectedEvent = fetchEventsFromID(eventId);
        System.out.println(selectedEvent.getLocation());
        eventName.setText(selectedEvent.getEventName());
        eventDate.setValue(selectedEvent.getDate());
        eventTime.setText(selectedEvent.getTime().toString());
        eventDuration.setText(Integer.toString(selectedEvent.getDuration()));
        priority.setValue(selectedEvent.getPriority());
        reminder.setValue(selectedEvent.getReminder());
        ArrayList<User> eventParticipants = fetchParticipants(eventId);
        String userIds = "";
        for (int i = 0; i< eventParticipants.size(); i++){
            String email = eventParticipants.get(i).getEmail();
            System.out.println("CHamou");
            userIds += email + "; ";
        }
        participants.setText(userIds);

        Location myLocation = fetchLocationFromEvent(eventId);
        eventLocation.setText(myLocation.getStreet() + ", " +  myLocation.getStreetNumber() + ", " +  myLocation.getZip() + ", " +  myLocation.getCity() + ", " +  myLocation.getCountry());


    }



    @FXML
    public void CreateEventOnAction(ActionEvent event) {

    }
}
