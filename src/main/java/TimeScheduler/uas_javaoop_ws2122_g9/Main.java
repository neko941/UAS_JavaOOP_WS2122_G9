package TimeScheduler.uas_javaoop_ws2122_g9;

import Controllers.CalendarController;
import Controllers.MultiThreading;
import Models.*;
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

import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.EventController.CreateEvent;
import static ExternalConnections.DBConn.getConnection;
import static ExternalConnections.DBUtilities.*;

//public class Main extends Application {
//   @Override
//   public void start(Stage stage) throws IOException {
//       Parent parent = FXMLLoader.load(getClass().getResource("/UI/LoginUI.fxml"));
//       stage.setScene(new Scene(parent));
//       stage.show();
//   }
//
//   public static void main(String[] args) {
//       MultiThreading EmailThread = new MultiThreading("Send Email Thread");
//       EmailThread.start();
//       launch();
//   }
//}

// public class Main {
//     public static void main(String[] args) {
//         Application.launch(CalendarController.class,args);
//     }
// }

public class Main {
    public static void main(String[] args) {
        DBUtilities();
        // System.out.println(getDataFromConfig("systemEmail", "password"));

        Location myLocation = new Location("street", 25, "zip", "city", "country", 3, 405);

        User user = new User("john", "jackson","johnjack155", "mypassword", "john_jack4321234@gmail");
        int id = insertNewUser(user);
        user.setId(id);

        ArrayList<User> participants = new ArrayList<User>();
        participants.add(user);
        Event myEvent = new Event("event created in the backend", LocalDate.of(2021, 12, 27),
                LocalTime.of(21, 12, 27),
                30,myLocation , participants,  null, Reminder.ONE_HOUR, Priority.LOW);
        CreateEvent(myEvent);


    }
}


