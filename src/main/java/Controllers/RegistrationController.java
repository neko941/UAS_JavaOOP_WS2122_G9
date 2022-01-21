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

import javafx.scene.Parent;
import javafx.scene.Scene;
import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.EmailUtils.verificationEmail;
import static Controllers.ColorController.changeTextFieldColor;
import static ExternalConnections.DBUtilities.insertNewUser;

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

    String verification;
    User user;

    public boolean checkAllTextField()
    {
        return Arrays   .asList(
                        // check username
                        changeTextFieldColor(   Validation.checkLength(usernameTextField.getText(), 6, 20),
                                usernameTextField.getText().isBlank(),
                                usernameLengthConstraint),
                        changeTextFieldColor(   Validation.checkNoSpace(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpaceConstraint),
                        changeTextFieldColor(   Validation.checkNoSpecialCharacter(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpecialCharacterConstraint),
                        // check email
                        changeTextFieldColor(   Validation.checkInputEmail(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailWarning),
                        // check password
                        changeTextFieldColor(   Validation.checkLength(passwordTextField.getText(), 6, 20),
                                passwordTextField.getText().isBlank(),
                                passwordLengthConstraint),
                        changeTextFieldColor(   Validation.checkUpper(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordUpperCaseConstraint),
                        changeTextFieldColor(   Validation.checkLower(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordLowerCaseConstraint),
                        changeTextFieldColor(   Validation.checkPunctuation(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordSpecialCharacterConstraint),
                        changeTextFieldColor(   Validation.checkDigit(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordDigitConstraint),
                        // check confirm password
                        changeTextFieldColor(   passwordTextField.getText().equals(confirmPasswordTextField.getText()),
                                confirmPasswordTextField.getText().isBlank(),
                                confirmPasswordWarning))
                .stream()
                .allMatch(val -> val == true);
    }

    public void registerUserOnAction(ActionEvent event) throws IOException {
        if (checkAllTextField())
        {
            // create verification code
            verification = Security.generateRandomNumber();
            // send code to user's email
            verificationEmail(emailTextField.getText(), verification);
            // save user info
            user = new User( emailTextField.getText(),
                    usernameTextField.getText(),
                    passwordTextField.getText(),
                    firstNameTextField.getText(),
                    lastNameTextField.getText());
            // confirm code
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/UI/RegistrationEmailConfirmUI.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        };

    }

    public void ContinueButtonOnAction(ActionEvent event) {
        if (((String)confirmCodeTextField.getText()).equals(verification))
        {
//            insertNewUser(user);
        }
    }
    public void CloseButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}