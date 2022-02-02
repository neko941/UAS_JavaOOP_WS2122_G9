package Controllers;

import Models.*;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DetailedWeekView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;
import org.w3c.dom.events.MouseEvent;

import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchAllEventsFromUser;

public class CalendarController extends Application {
    public DetailedWeekView timetable_week  = new DetailedWeekView();
    public Button createButton = new Button("New Appointment");
    public Button editButton = new Button("Edit Appointment");
    private User currentUser;

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        timetable_week.setStartTime(LocalTime.of(9,0));
        timetable_week.setEndTime(LocalTime.of(17,0));
        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        CalendarView calendarView = new CalendarView();

        CalendarSource myCalendarSource = new CalendarSource("Priority:");

        Calendar lowPrio = new Calendar ("Low Priority");
        Calendar medPrio = new Calendar ("Medium Priority");
        Calendar highPrio = new Calendar ("High Priority");

        lowPrio.setStyle(Calendar.Style.STYLE1);
        medPrio.setStyle(Calendar.Style.STYLE3);
        highPrio.setStyle(Calendar.Style.STYLE5);

        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.showWeek(Year.of(2022),4);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowDeveloperConsole(true);
        calendarView.setShowDeveloperConsole(true);

        myCalendarSource.getCalendars().setAll(highPrio, medPrio, lowPrio);

        DBUtilities();
        showEvents(fetchAllEventsFromUser(currentUser),lowPrio, medPrio, highPrio);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                        highPrio.clear();
                        medPrio.clear();
                        lowPrio.clear();
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

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                EntryEventHandler(e);
            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                EditEventHandler(e);
            }
        });

        root.getChildren().add(calendarView);
        root.getChildren().add(createButton);
        root.getChildren().add(editButton);

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

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

    private void EntryEventHandler(ActionEvent event) {
        try {
//            CreateEventController createController = new CreateEventController();
//            createController.setCurrentUser(currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            createController.start(stage);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    private void EditEventHandler(ActionEvent event) {
        try {
            EditDeleteEventController editController = new EditDeleteEventController();
            editController.setCurrentUser(currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            editController.start(stage);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

