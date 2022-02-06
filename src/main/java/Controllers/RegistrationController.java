/**
 * Author: neko941
 * Created on: 2022-01-01
 */

package Controllers;

import Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import javafx.scene.Parent;
import javafx.scene.Scene;

import static Controllers.ColorController.changeLabelText;
import static Controllers.Debugging.printNotificationInConsole;
import static Controllers.EmailUtils.verificationEmail;
import static Controllers.ColorController.changeLabelColor;
import static ExternalConnections.DBUtilities.*;

public class RegistrationController {
    @FXML private Button closeButton;

    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField confirmCodeTextField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private PasswordField confirmPasswordTextField;

    @FXML private Label usernameLengthConstraint;
    @FXML private Label usernameSpecialCharacterConstraint;
    @FXML private Label usernameSpaceConstraint;
    @FXML private Label emailWarning;
    @FXML private Label confirmPasswordWarning;
    @FXML private Label passwordLengthConstraint;
    @FXML private Label passwordUpperCaseConstraint;
    @FXML private Label passwordLowerCaseConstraint;
    @FXML private Label passwordSpecialCharacterConstraint;
    @FXML private Label passwordDigitConstraint;
    @FXML private Label emailAvailableWarning;
    @FXML private Label usernameAvailableWarning;

    public static String verification;
    public static User user;
    public static String tempEmail;

    public boolean checkAllTextField()
    {
        return Stream.of(
                        // check username
                        changeLabelColor(
                                Validation.checkLength(usernameTextField.getText(), 6, 20),
                                usernameTextField.getText().isBlank(),
                                usernameLengthConstraint),
                        changeLabelColor(
                                Validation.checkNoSpace(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpaceConstraint),
                        changeLabelColor(
                                Validation.checkNoSpecialCharacter(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpecialCharacterConstraint),
                        changeLabelText(
                                isUsernameAvailable(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameAvailableWarning,
                                "",
                                "Username already exists"),
                        // check email
                        changeLabelColor(
                                Validation.checkInputEmail(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailWarning),
                        changeLabelText(
                                isEmailAvailable(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailAvailableWarning,
                                "",
                                "Email already exists"),
                        // check password
                        changeLabelColor(
                                Validation.checkLength(passwordTextField.getText(), 6, 20),
                                passwordTextField.getText().isBlank(),
                                passwordLengthConstraint),
                        changeLabelColor(
                                Validation.checkUpper(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordUpperCaseConstraint),
                        changeLabelColor(
                                Validation.checkLower(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordLowerCaseConstraint),
                        changeLabelColor(
                                Validation.checkPunctuation(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordSpecialCharacterConstraint),
                        changeLabelColor(
                                Validation.checkDigit(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordDigitConstraint),
                        // check confirm password
                        changeLabelColor(
                                passwordTextField.getText().equals(confirmPasswordTextField.getText()),
                                confirmPasswordTextField.getText().isBlank(),
                                confirmPasswordWarning))
                .allMatch(val -> val);
    }

    public void registerUserOnAction(ActionEvent event) throws IOException {
        tempEmail = emailTextField.getText();
        if (checkAllTextField())
        {
            // create verification code
            verification = Security.generateRandomNumber();
            printNotificationInConsole(String.format("Verification code generated \t%s", verification));
            // send code to user's email
            verificationEmail(emailTextField.getText(), verification);
            // save user info
            user = new User(
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    usernameTextField.getText(),
                    passwordTextField.getText(),
                    emailTextField.getText()
                    );
            // confirm code
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/RegistrationEmailConfirmUI.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.format("Error: %s\n", e.getMessage());
            }
        };

    }

    public void continueButtonOnAction(ActionEvent event) throws IOException {
        if (verification.equals(confirmCodeTextField.getText()))
        {
            printNotificationInConsole("Create account successfully");

            insertNewUser(user);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginUI.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
        {
            printNotificationInConsole("Wrong confirmation code");
        }
    }
    public void CloseButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void SignInButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginUI.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    public void resendEmailButtonOnAction(ActionEvent event)
    {
        // create verification code
        verification = Security.generateRandomNumber();
        printNotificationInConsole(String.format("Verification code generated \t%s", verification));
        // send code to user's email
        verificationEmail(tempEmail, verification);
    }
}