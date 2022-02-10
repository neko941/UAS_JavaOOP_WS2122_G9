/**
 * @author neko941, klangthang
 * Created on: 2022-01-21
 */
package Controllers;

import ExternalConnections.DBUtilities;

import Models.User;
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
import java.util.stream.Stream;

import static Controllers.ColorController.changeLabelText;
import static Controllers.Debugging.printNotificationInConsole;
import static Controllers.ConfigController.getDataFromConfig;

public class LoginController {
    @FXML private Label LoginMessageLabel;
    @FXML private TextField usernameLogin;
    @FXML private PasswordField PasswordLogin;

    /**
     * Log in button
     *
     * @param event when user clicks on "Log-in" button
     */
    public void loginButtonOnAction(ActionEvent event) throws InterruptedException {
        boolean check = changeLabelText(
                DBUtilities.verifyUser(usernameLogin.getText(), PasswordLogin.getText()),
                Stream.of(
                                usernameLogin.getText().isBlank(),
                                PasswordLogin.getText().isBlank())
                        .allMatch(val -> val),
                LoginMessageLabel,
                "Congratulations!",
                "Invalid Login. Please try again");

        if(check && usernameLogin.getText().equals(getDataFromConfig("adminAccount", "username"))
                && PasswordLogin.getText().equals(getDataFromConfig("adminAccount", "password"))){
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/admin_edit2.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.format("Error: %s\n", e.getMessage());
            }
        }
        else if (check)
        {
            printNotificationInConsole("Successfully log-in as " + usernameLogin.getText());

            try {
                User currentUser = DBUtilities.fetchUser(usernameLogin.getText());
                CalendarController calendarController = new CalendarController();
                calendarController.setCurrentUser(currentUser);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                calendarController.start(stage);
            } catch (IOException e) {
                System.out.format("Error: %s\n", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sign-up button
     *
     * @param event when user clicks on "Sign-up" button
     */
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

    /**
     * Used when a user wants to reset their password
     *
     * @param event when user clicks on "Forgot Password" button
     */
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

