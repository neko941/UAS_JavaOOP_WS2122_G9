/**
 * Author: neko941
 * Created on: 2022-01-21
 */
package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static Controllers.ColorController.changeLabelColor;
import static Controllers.ColorController.changeLabelText;
import static Controllers.EmailUtils.verificationEmail;
import static ExternalConnections.DBUtilities.isEmailAvailable;
import static ExternalConnections.DBUtilities.isUsernameAvailable;

public class ForgetPasswordController {
    @FXML private Label emailWarning;
    @FXML private Label confirmNewPasswordWarning;
    @FXML private Label passwordLengthConstraint;
    @FXML private Label passwordUpperCaseConstraint;
    @FXML private Label passwordLowerCaseConstraint;
    @FXML private Label passwordSpecialCharacterConstraint;
    @FXML private Label passwordDigitConstraint;
    @FXML private Button SignUpButton;
    @FXML private Button sendEmailButton;
    @FXML private TextField emailTextField;
    @FXML private TextField otpTextField;

    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField confirmNewPassWordTextField;

    String verification;

    public boolean checkAllTextFieldForgotPasswordUI()
    {
        return Arrays.asList(
                        // check email
                        changeLabelColor(   Validation.checkInputEmail(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailWarning),
                        changeLabelText(
                                !isEmailAvailable(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailInDatabaseWarning,
                                "Email does not exist in the database"))
                .allMatch(val -> val);
    }

    public boolean checkAllTextFieldResetPasswordUI()
    {
        return Arrays.asList(
                        // check password
                        changeTextFieldColor(   Validation.checkLength(newPasswordTextField.getText(), 6, 20),
                                newPasswordTextField.getText().isBlank(),
                                passwordLengthConstraint),
                        changeTextFieldColor(   Validation.checkUpper(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordUpperCaseConstraint),
                        changeTextFieldColor(   Validation.checkLower(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordLowerCaseConstraint),
                        changeTextFieldColor(   Validation.checkPunctuation(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordSpecialCharacterConstraint),
                        changeTextFieldColor(   Validation.checkDigit(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordDigitConstraint),
                        // check confirm password
                        changeTextFieldColor(   newPasswordTextField.getText().equals(confirmNewPassWordTextField.getText()),
                                confirmNewPassWordTextField.getText().isBlank(),
                                confirmNewPasswordWarning))
                .stream()
                .allMatch(val -> val == true);
    }

    public void sendEmailButtonOnAction(ActionEvent event) {
        if (checkAllTextFieldForgotPasswordUI()) {
            verification = Security.generateRandomString(); /*generate random string*/
            verificationEmail(emailTextField.getText(), verification);
            sendEmailButton.setText("Resend Email");

        }
    }

    public void continueButtonOnAction(ActionEvent event) {
        if (((String)otpTextField.getText()).equals(verification))
        {
            // confirm code
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/UI/ResetPasswordUI.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
    }

    public void resetPasswordOnAction(ActionEvent event) {
        if (checkAllTextFieldResetPasswordUI())
        {
            // change password in database

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/UI/LoginUI.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
    }

    public void SignUpButtonOnAction(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UI/RegistrationUI.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }


}
