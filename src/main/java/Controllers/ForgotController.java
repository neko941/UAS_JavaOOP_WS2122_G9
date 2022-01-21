package Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import static Controllers.EmailUtils.verificationEmail;

public class ForgotController {
    
    @FXML private TextField emailTextField;
    @FXML private TextField otpTextField;

    String verification;

    public void sendEmailButtonOnAction(ActionEvent event) {
        verification = Security.generateRandomString(); /*generate random string*/
        verificationEmail(emailTextField.getText(), verification);
    }

    public void continueButtonOnAction(ActionEvent event) {
        if (!((String)otpTextField.getText()).equals(verification))
        {

        }
    }
}
