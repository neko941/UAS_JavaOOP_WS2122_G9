/**
 * @author neko941
 */
package Controllers;

import Models.*;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;

import static Controllers.Debugging.printNotificationInConsole;
import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchAllEventsFromUser;

@SuppressWarnings("ALL")
public class CalendarController extends Application {
    @FXML private Button createButton;
    @FXML private Button editButton;
    @FXML private CalendarView calendarView;
    @FXML private Button userlable;
    private Thread updateTimeThread;
    public User currentUser;

    /**
     * Sets the current logged in user which will be used to fetch and create events.
     *
     * @param: currentUser: object of type User
     *
     * @return: void
     */
    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    /**
     * Loads the Calendar UI, initial events and creates thread which will update events and current time every 10s.
     *
     * @param: primaryStage - Stage on which the page will be rendered
     * @return: void
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resourceUrl = getClass().getResource("/UI/CalendarNewUI.fxml");
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        loader.setController(this);
        Parent root = loader.load();

        CalendarSource myCalendarSource = new CalendarSource("Priority:");

        Calendar lowPrio = new Calendar ("Low Priority");
        Calendar medPrio = new Calendar ("Medium Priority");
        Calendar highPrio = new Calendar ("High Priority");

        lowPrio.setStyle(Calendar.Style.STYLE1);
        medPrio.setStyle(Calendar.Style.STYLE3);
        highPrio.setStyle(Calendar.Style.STYLE5);

        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());
        calendarView.showWeek(Year.of(LocalDate.now().getYear()), LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
        calendarView.setShowAddCalendarButton(false);


        myCalendarSource.getCalendars().setAll(highPrio, medPrio, lowPrio);

        DBUtilities();
        showEvents(fetchAllEventsFromUser(currentUser),lowPrio, medPrio, highPrio);

        updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                        highPrio.clear();
                        medPrio.clear();
                        lowPrio.clear();
                        printNotificationInConsole("fetching events from: " + currentUser.getEmail());
                        showEvents(fetchAllEventsFromUser(currentUser),lowPrio, medPrio, highPrio);
                    });
                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        Scene scene = new Scene(root);
        userlable.setText(currentUser.getUsername());
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Gets all events which the current user is part of and places them in the according priority calendar.
     *
     * @param: MyEvents - all events from a respective user
     * @return: void
     */
    private void showEvents(ArrayList<Event> myEvents, Calendar lowPrio, Calendar medPrio, Calendar highPrio){
        for (int i = 0; i < myEvents.size(); i++) {
            Entry<String> myEntry = new Entry<>();
            Event thisEvent = myEvents.get(i);

            String title = thisEvent.getEventName();
            LocalDateTime dateTime = LocalDateTime.of(thisEvent.getDate(), thisEvent.getTime());
            int duration = thisEvent.getDuration();
            String id = String.valueOf(thisEvent.getEventID());
            Location location = thisEvent.getLocation();

            myEntry.setTitle(title);
            myEntry.setId(id);
            myEntry.setInterval(dateTime, dateTime.plusMinutes(duration));
            myEntry.setLocation(location.toString());

            if (myEvents.get(i).getPriority() == Priority.HIGH){
                highPrio.addEntry(myEntry);
            } else if (myEvents.get(i).getPriority() == Priority.MEDIUM){
                medPrio.addEntry(myEntry);
            } else if (myEvents.get(i).getPriority() == Priority.LOW){
                lowPrio.addEntry(myEntry);
            }
        }
    }

    /**
     * Opens a new create event window and sets it's controller and current user.
     *
     * @param: event - ActionEvent, in this case the clicking of a button
     * @return: void
     */
    @FXML
    private void CreateButtonOnAction(ActionEvent event) {
        try {
            CreateEventController createController = new CreateEventController();
            createController.setCurrentUser(currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            createController.start(stage);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    /**
     * Opens a new edit/view event window and sets it's controller and current user.
     *
     * @param: event - ActionEvent, in this case the clicking of a button
     * @return: void
     */
    @FXML
    private void EditButtonOnAction(ActionEvent event) {
        try {
            EditDeleteEventController editController = new EditDeleteEventController();
            editController.setCurrentUser(currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            editController.start(stage);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    @FXML
    private void ExportButtonOnAction(ActionEvent event) {
        ExportTXT txt = new ExportTXT();
        txt.export("Event");
    }

    /**
     * Clears the current user data, stops the thread, and loads the login screen.
     *
     * @param: event - ActionEvent, in this case the clicking of a button
     * @return: void
     */
    @FXML
    private void LogoutButtonOnAction(ActionEvent event) {
        try {
            updateTimeThread.stop();
            currentUser = null;
            Parent parent = FXMLLoader.load(getClass().getResource("/UI/LoginUI.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setWidth(550);
            stage.setHeight(580);
            stage.show();
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    /**
     * Opens a new create event window and sets it's controller and current user.
     *
     * @param: event - ActionEvent, in this case the clicking of a button
     * @return: void
     */
    @FXML
    private void UserProfileButtonOnAction(ActionEvent event){
        try{
            UserProfileController userProfileController = new UserProfileController();
            userProfileController.setCurrentUser(currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            userProfileController.start(stage);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

