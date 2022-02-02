package Controllers;

import Models.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;

import static Controllers.EventController.CreateEvent;
import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchUser;
import static java.lang.Integer.parseInt;


public class CreateEventController extends Application {
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
    @FXML private Label userlable;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtilities();
        URL resourceUrl = getClass().getResource("/UI/CreateEventUI.fxml");
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        loader.setController(this);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        userlable.setText(currentUser.getUsername());
        stage.show();
    }

    public void cancelButtonOnAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void CreateEventOnAction(ActionEvent event) {
        Priority selectedPriority = mapPriority(priority.getValue().toString());
        Reminder selectedReminder = mapReminder(reminder.getValue().toString());
        String[] emails = participants.getText().split(",");
        LocalTime.of(parseInt(eventTime.getText().split(":")[0]),
            parseInt(eventTime.getText().split(":")[1]));
        parseInt(eventDuration.getText());
        //TODO: add field validation
        if (true) {
            ArrayList<User> mappedParticipants = new ArrayList<User>();
            mappedParticipants.add(currentUser);
                for (int i = 0; i < emails.length; i++){
                    User myUser = fetchUser(emails[i]);
                    mappedParticipants.add(myUser);
                }
                String[] locationData = eventLocation.getText().split(",");
                Event myEvent = new Event(eventName.getText(),
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
                CreateEvent(myEvent);
                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.close();

        };
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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

