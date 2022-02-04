package TimeScheduler.uas_javaoop_ws2122_g9;

import Controllers.CalendarController;
import Controllers.MultiThreading;
import ExternalConnections.DBUtilities;
import Models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static Controllers.ConfigController.getDataFromConfig;
import ExternalConnections.*;

import Controllers.CalendarController;
import Controllers.MultiThreading;
import Models.Event;
import Models.Priority;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.Debugging.*;
import static Controllers.EmailUtils.*;
import static ExternalConnections.DBConn.getConnection;
import static ExternalConnections.DBUtilities.*;

public class Main extends Application {
   @Override
   public void start(Stage stage) throws IOException
   {
       Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginUI.fxml")));
       stage.setScene(new Scene(parent));
       stage.show();
   }

   public static void main(String[] args)
   {
       DBUtilities.DBUtilities();
       MultiThreading EmailThread = new MultiThreading("Send Email Thread");
       EmailThread.start();
       launch();
   }
}

// public class Main {
//     public static void main(String[] args) {
//         Application.launch(CalendarController.class,args);
//     }
// }

// public class Main {
//     public static void main(String[] args) {
////         verificationEmail("nguyenkhoa090401@gmail.com", "1234567");
////         System.out.println(Reminder.defaultOr(Reminder.TEN_MINUTES));
//     }
// }

//public class Main {
//    public static void main(String[] args) {
////        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////        printNotificationInConsole("ok");
//        LocalDate date = LocalDate.parse("2022-02-04");
//        LocalTime time = LocalTime.parse("15:25:00");
//
//        LocalDateTime reminderTime = Reminder.TEN_MINUTES.getReminderTime(LocalDateTime.of(date, time));
////        LocalDateTime time2 = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
//        LocalDateTime now = LocalDateTime.now();
//
//        System.out.println(reminderTime);
//        System.out.println(now);
//
//        System.out.println(reminderTime.isBefore(now));
//    }
//}

// @SuppressWarnings("ALL")
//public class Main {
//    public static void main(String[] args)
//    {
//        DBUtilities();
//        User user = fetchUser("neko941");
//
//        Event event0 = new Event(
//                "event0",
//                LocalDate.parse("2022-02-01"),
//                LocalTime.parse("19:00"),
//                15,
//                new Location("1", -1, "", "", "", -1, -1),
//                null,
//                null,
//                null,
//                Reminder.TEN_MINUTES,
//                Priority.HIGH);
//
//        Event event1 = new Event(
//                "event1",
//                LocalDate.parse("2022-02-02"),
//                LocalTime.parse("19:00"),
//                15,
//                new Location("1", -1, "", "", "", -1, -1),
//                null,
//                null,
//                null,
//                Reminder.TEN_MINUTES,
//                Priority.HIGH);
//
//        eventEmail(2,"nguyenkhoa090401@gmail.com", event0);
//
//        int eventId = insertNewEvent(event0);
//        event0.setEventID(eventId);
//        createUser_EventBridge(user.getId(), event0.getEventID());
////
////        int eventId1 = insertNewEvent(event1);
////        event1.setEventID(eventId1);
////        createUser_EventBridge(user.getId(), event1.getEventID());
//
//        ArrayList<Event> temp = fetchAllEventsFromUser(user);
//        for (Event event : temp)
//        {
//            System.out.println(event.getEventName());
//        }
//    }
//}


