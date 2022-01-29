package Controllers;

import Models.Event;
import Models.Location;
import Models.Priority;
import Models.User;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;
import org.w3c.dom.events.MouseEvent;

import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchAllEventsFromUser;

public class CalendarController extends Application {

    private User currentUser = new User("john", "jackson","johnjack155", "mypassword", "john_jack4321234@gmail");

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {



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
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowDeveloperConsole(true);
        calendarView.s
        Scene scene = new Scene(calendarView);
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
            Location location = thisEvent.getLocation();

            myEntry.setTitle(title);
            myEntry.setInterval(dateTime, dateTime.plusMinutes(duration));
            myEntry.setLocation(location.toString());

            if (myEvents.get(i).getPriority() == Priority.HIGH){
                highPrio.addEntry(myEntry);
            }else if (myEvents.get(i).getPriority() == Priority.MEDIUM){
                medPrio.addEntry(myEntry);
            } else if (myEvents.get(i).getPriority() == Priority.LOW){
                lowPrio.addEntry(myEntry);
                highPrio.addEntry(myEntry);
            }
        }
    }

    @FXML
    public void handleButtonAction(MouseEvent event) {
        System.out.println(event.getTarget());
        System.out.println(event.getTarget().toString());
    }
//    public static void main(String[] args) {
//        launch(args);
//    }
}

