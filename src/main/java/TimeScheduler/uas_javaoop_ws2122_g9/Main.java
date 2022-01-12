package TimeScheduler.uas_javaoop_ws2122_g9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//public class Main
//{
//    public static void main(String[] args)
//    {
//        if(Validation.checkNoSpecialCharacter(" "))
//        {
//            System.out.println("yes");
//        }
//        else
//        {
//            System.out.println("no");
//        }
//    }
//}