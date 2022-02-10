package TimeScheduler.uas_javaoop_ws2122_g9;


import ExternalConnections.DBUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import Controllers.MultiThreading;
import java.util.Objects;


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




