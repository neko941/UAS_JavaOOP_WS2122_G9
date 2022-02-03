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
import java.time.LocalDateTime;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.Debugging.*;
import static Controllers.EmailUtils.verificationEmail;
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
//        printNotificationInConsole("ok");
//    }
//}

//public class Main {
//    public static void main(String[] args) {
//        DBUtilities.DBUtilities();
//        User user = new User("", "","neko941", "!No123", "nguyenkhoa090401@gmail");
//        printUserInfo(user);
//        Event event1 = new Event("event1", LocalDate.parse("2022-02-01"), LocalTime.parse("15:30"), 15, new Location("1", "", "", "", "", "", ""), null, null, Reminder.ONE_HOUR, Priority.HIGH);
//        printEventInfo(event1);
//        insertNewEvent(event1);
//        Event event2 = new Event("event2", LocalDate.parse("2022-02-01"), LocalTime.parse("19:30"), 15, new Location("2", "", "", "", "", "", ""), null, null, Reminder.ONE_HOUR, Priority.HIGH);
//        printEventInfo(event2);
//        insertNewEvent(event2);
//
//        ArrayList<Event> temp = fetchAllEventsFromUser(user);
//        for (Event event : temp) {
//            printEventInfo(event);
//        }
//    }
//}


