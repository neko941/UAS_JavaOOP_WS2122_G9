# UAS_JavaOOP_WS2122_G9

If you want to start the LoginUI please comment

```
public class Main {
    public static void main(String[] args) {
        Application.launch(CalendarController.class,args);
    }
}
```

and recomment

```
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
//        ExportTxt ExportTxt = new ExportTxt();
//        ExportTxt.export("users");
    }
}
```
