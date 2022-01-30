package TimeScheduler.uas_javaoop_ws2122_g9;

import Controllers.CalendarController;
import Controllers.MultiThreading;
import ExternalConnections.DBUtilities;
import Models.Event;
import Models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static Controllers.ConfigController.getDataFromConfig;
import ExternalConnections.*;

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
import static Controllers.EmailUtils.verificationEmail;
import static ExternalConnections.DBConn.getConnection;
import static ExternalConnections.DBUtilities.fetchAllEventsFromUser;
import static ExternalConnections.DBUtilities.isEmailAvailable;

public class Main extends Application {
   @Override
   public void start(Stage stage) throws IOException {
       Parent parent = FXMLLoader.load(getClass().getResource("/UI/LoginUI.fxml"));
       stage.setScene(new Scene(parent));
       stage.show();
   }

   public static void main(String[] args) {
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
//         verificationEmail("nguyenkhoa090401@gmail.com", "sdjajs");
//     }
// }

//public class Main {
//    public static void main(String[] args) {
////        User user = new User("john", "jackson","johnjack155", "mypassword", "john_jack4321234@gmail");
////        ArrayList<Event> events = fetchAllEventsFromUser(user);
////        System.out.println(events);
//        DBUtilities.DBUtilities();
//        System.out.println(isEmailAvailable("john_jack4321234@gmai"));
//    }
//}


