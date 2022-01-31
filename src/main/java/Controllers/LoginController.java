package Controllers;

import ExternalConnections.DBUtilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML private Label LoginMessageLabel;
    @FXML private TextField usernameLogin;
    @FXML private PasswordField PasswordLogin;

    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameLogin.getText().isBlank() && !PasswordLogin.getText().isBlank()) {
            if (DBUtilities.verifyUser(usernameLogin.getText(), PasswordLogin.getText())) {
                LoginMessageLabel.setText("Congratulations!");

                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/CalendarUI.fxml")));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.out.format("Error: %s\n", e.getMessage());
                }
            } else {
                LoginMessageLabel.setText("Invalid Login. Please try again");
            }
        } else {
            LoginMessageLabel.setText("Please enter email and password");
        }
    }

    @FXML
    public void SignUpButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/RegistrationUI.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.format("Error: %s\n", e.getMessage());
        }
    }


    @FXML
    public void ForgotButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ForgotPasswordUI.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.format("Error: %s\n", e.getMessage());
        }
    }
}

