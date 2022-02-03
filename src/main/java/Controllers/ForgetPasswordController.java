/**
 * @author neko941
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import static Controllers.ColorController.changeLabelColor;
import static Controllers.ColorController.changeLabelText;
import static Controllers.Debugging.printNotificationInConsole;
import static Controllers.EmailUtils.verificationEmail;
import static ExternalConnections.DBUtilities.isEmailAvailable;

public class ForgetPasswordController {
    @FXML private Label emailWarning;
    @FXML private Label confirmNewPasswordWarning;
    @FXML private Label passwordLengthConstraint;
    @FXML private Label passwordUpperCaseConstraint;
    @FXML private Label passwordLowerCaseConstraint;
    @FXML private Label passwordSpecialCharacterConstraint;
    @FXML private Label passwordDigitConstraint;
    @FXML private Label emailInDatabaseWarning;
    @FXML private Button sendEmailButton;
    @FXML private TextField emailTextField;
    @FXML private TextField otpTextField;

    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField confirmNewPassWordTextField;

    public static String verification;

    /**
     *
     * @return true if all text fields in ForgotPasswordUI are in correct format
     */
    public boolean checkAllTextFieldForgotPasswordUI()
    {
        return Stream.of(
                        // check email
                        changeLabelColor(
                                Validation.checkInputEmail(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailWarning),
                        changeLabelText(
                                !isEmailAvailable(emailTextField.getText()),
                                emailTextField.getText().isBlank(),
                                emailInDatabaseWarning,
                                "Please check your email",
                                "Email does not exist in the database"))
                .allMatch(val -> val);
    }

    /**
     *
     * @return true if all text fields in ResetPasswordUI are in correct format
     */
    public boolean checkAllTextFieldResetPasswordUI()
    {
        return Stream.of(
                        // check password
                        changeLabelColor(
                                Validation.checkLength(newPasswordTextField.getText(), 6, 20),
                                newPasswordTextField.getText().isBlank(),
                                passwordLengthConstraint),
                        changeLabelColor(
                                Validation.checkUpper(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordUpperCaseConstraint),
                        changeLabelColor(
                                Validation.checkLower(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordLowerCaseConstraint),
                        changeLabelColor(
                                Validation.checkPunctuation(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordSpecialCharacterConstraint),
                        changeLabelColor(
                                Validation.checkDigit(newPasswordTextField.getText()),
                                newPasswordTextField.getText().isBlank(),
                                passwordDigitConstraint),
                        // check confirm password
                        changeLabelColor(
                                newPasswordTextField.getText().equals(confirmNewPassWordTextField.getText()),
                                confirmNewPassWordTextField.getText().isBlank(),
                                confirmNewPasswordWarning))
                .allMatch(val -> val);
    }

    /**
     * Send email when user clicks on "Send Email" button
     *
     * @param event when user clicks on "Send Email" button
     */
    public void sendEmailButtonOnAction(ActionEvent event) {
        if (checkAllTextFieldForgotPasswordUI()) {
            verification = Security.generateRandomString();
            printNotificationInConsole(String.format("Verification code generated \t%s", verification));
            verificationEmail(emailTextField.getText(), verification);
            sendEmailButton.setText("Resend Email");
        }
    }

    /**
     * Check verification code when user clicks on "Continue" button. If true, open "ResetPasswordUI"
     *
     * @param event when user clicks on "Continue" button
     */
    public void continueButtonOnAction(ActionEvent event) {
        if (verification.equals(otpTextField.getText()))
        {
            // confirm code
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/ResetPasswordUI.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.format("Error: %s\n", e.getMessage());
            }
        }
    }

    /**
     * Used when a user wants to reset their password
     *
     * @param event when user clicks on "Reset Password" button
     */
    public void resetPasswordOnAction(ActionEvent event) {
        if (checkAllTextFieldResetPasswordUI())
        {
            //TODO: change password in database

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI/LoginUI.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.format("Error: %s\n", e.getMessage());
            }
        }
    }

    /**
     * Used when a user wants to go back to the registration tab
     *
     * @param event when user clicks on "Reset Password" button
     */
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
}
