/**
 * Author: neko941
 * Created on: 2022-01-01
 */

package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class RegistrationController {
    @FXML private Button closeButton;

    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
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
            verification = Security.generateRandomNumber();
            verificationEmail(emailTextField.getText(), verification);
        };
    }

    public void CloseButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}