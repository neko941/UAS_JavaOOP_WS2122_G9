# UAS_JavaOOP_WS2122_G9

Github username with full name and ID number

Trung Nguyen Quoc (1370166): tysunqua

Khang Nguyen Phu (1370289): klangthang

Khoa Nguyen (1370247): neko941

Jatender Singh Jossan (1346747): jatenderjossan

Matheus Mapa de Oliveira (1307964): Matheus-Mapa


If you want to start the LoginUI please use

```
public class Main extends Application {
   @Override
   public void start(Stage stage) throws IOException {
       Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginUI.fxml")));
       stage.setScene(new Scene(parent));
       stage.show();
   }

   public static void main(String[] args) {
       DBUtilities.DBUtilities();
       MultiThreading EmailThread = new MultiThreading("Send Email Thread");
       EmailThread.start();
       launch();
   }
}
```
