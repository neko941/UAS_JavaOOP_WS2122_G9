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
import javafx.stage.Stage;

import static Controllers.EmailUtils.verificationEmail;
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

    String errorColor = "red";
    String validColor ="green";
    String defaultColor = "black";

    String verification;
    User user;

    public boolean checkValidAndChangeColor(boolean check,boolean space, Label label)
    {
        if(space)
        {
            label.setStyle("-fx-text-fill:" + defaultColor);
        }
        else {
            if (check)
            {
                label.setStyle("-fx-text-fill:" + validColor);
            }
            else
            {
                label.setStyle("-fx-text-fill:" + errorColor);
            }
        }
        return check;
    }

    public boolean checkAllTextField()
    {
        return Arrays   .asList(
                        // check username
                        checkValidAndChangeColor(   Validation.checkLength(usernameTextField.getText(), 6, 20),
                                usernameTextField.getText().isBlank(),
                                usernameLengthConstraint),
                        checkValidAndChangeColor(   Validation.checkNoSpace(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpaceConstraint),
                        checkValidAndChangeColor(   Validation.checkNoSpecialCharacter(usernameTextField.getText()),
                                usernameTextField.getText().isBlank(),
                                usernameSpecialCharacterConstraint),
                        // check email
                        checkValidAndChangeColor(   Validation.checkInputEmail(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailWarning),
                        // check password
                        checkValidAndChangeColor(   Validation.checkLength(passwordTextField.getText(), 6, 20),
                                passwordTextField.getText().isBlank(),
                                passwordLengthConstraint),
                        checkValidAndChangeColor(   Validation.checkUpper(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordUpperCaseConstraint),
                        checkValidAndChangeColor(   Validation.checkLower(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordLowerCaseConstraint),
                        checkValidAndChangeColor(   Validation.checkPunctuation(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordSpecialCharacterConstraint),
                        checkValidAndChangeColor(   Validation.checkDigit(passwordTextField.getText()),
                                passwordTextField.getText().isBlank(),
                                passwordDigitConstraint),
                        // check confirm password
                        checkValidAndChangeColor(   passwordTextField.getText().equals(confirmPasswordTextField.getText()),
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
        if (!((String)confirmCodeTextField.getText()).equals(verification))
        {
//            insertNewUser(user);
        }
    }
    public void CloseButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}