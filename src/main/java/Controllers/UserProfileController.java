package Controllers;

import Models.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static ExternalConnections.DBUtilities.DBUtilities;
import static ExternalConnections.DBUtilities.fetchUser;

public class UserProfileController extends Application {
    @FXML private Label FirstNameLabel;
    @FXML private Label LastNameLabel;
    @FXML private Label EmailLabel;
    @FXML private Button ResetPasswordButton;
    @FXML private Button CloseButton;
    private User currentUser;

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }



    public void CloseButtonOnAction(ActionEvent event){
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void ResetPasswordOnAction(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ForgotPasswordUI.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.format("Error: %s\n", e.getMessage());
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtilities();
        URL resourceUrl = getClass().getResource("/UI/UserProfileUI.fxml");
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        loader.setController(this);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        // here is a feature not a bug
        FirstNameLabel.setText(currentUser.getFirstname());
        LastNameLabel.setText(currentUser.getLastname());
        EmailLabel.setText(currentUser.getEmail());
        stage.show();
    }
}
