package Controllers;

import Models.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;

import static Controllers.EventController.CreateEvent;
import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchUser;
import static java.lang.Integer.parseInt;


public class CreateEventController extends EventUIController {
    @FXML private Label userlable;


    /**
     * Loads the Create event UI.
     *
     * @param: primaryStage - Stage on which the page will be rendered
     * @return: void
     */
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
        errorLabel.setText("");
        participants.setText(currentUser.getEmail() + ", ");
        stage.show();
    }

    /**
     * Creates a new Event based on information filled out in the UI form and closes the Create event window.
     *
     * @return: void
     */
    @FXML
    public void CreateEventOnAction() {
        Priority selectedPriority = mapPriority(priority.getValue().toString());
        Reminder selectedReminder = mapReminder(reminder.getValue().toString());
        String[] emails = participants.getText().replaceAll("\\s","").split(",");


        if (!eventName.getText().trim().isEmpty() &&
            !eventTime.getText().trim().isEmpty() &&
            !eventMinutes.getText().trim().isEmpty() &&
            !eventDuration.getText().trim().isEmpty() &&
            !eventStreet.getText().trim().isEmpty() &&
            !eventHouseNr.getText().trim().isEmpty() &&
            !eventZipCode.getText().trim().isEmpty() &&
            !eventCity.getText().trim().isEmpty() &&
            !eventCountry.getText().trim().isEmpty() ) {

            errorLabel.setText("");
            ArrayList<User> mappedParticipants = new ArrayList<User>();
            for (int i = 0; i < emails.length; i++){
                User myUser = fetchUser(emails[i]);
                mappedParticipants.add(myUser);
            }

            Event myEvent = new Event(eventName.getText(),
                    eventDate.getValue(),
                    LocalTime.of(parseInt(eventTime.getText()),
                            parseInt(eventMinutes.getText())),
                    parseInt(eventDuration.getText()),
                    new Location(eventStreet.getText().replaceAll("\\s",""),
                            eventHouseNr.getText().replaceAll("\\s",""),
                            eventZipCode.getText().replaceAll("\\s",""),
                            eventCity.getText().replaceAll("\\s",""),
                            eventCountry.getText().replaceAll("\\s",""),
                            "",
                            ""),
                    mappedParticipants,
                    emails,
                    attachment,
                    selectedReminder,
                    selectedPriority);
            CreateEvent(currentUser, myEvent);
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.close();
        }
        else{
            errorLabel.setText("Missing Data!");
        }
    }

}

