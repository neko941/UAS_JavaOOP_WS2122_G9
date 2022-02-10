//package Controllers;
//
//import ExternalConnections.DBConn;
//import ExternalConnections.DBUtilities;
//import Models.User;
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.stage.Stage;
//
//
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ResourceBundle;
//
//import static ExternalConnections.DBUtilities.DBUtilities;
//import static ExternalConnections.DBUtilities.fetchAllUsersrFromDatabase;
//
//public class AdminController implements Initializable {
//    @FXML private ScrollPane userScrollPane;
//    @FXML  TableView <User> userTableView;
//    @FXML  TableColumn<User, String>  usernameCol;
//    @FXML  TableColumn<User, String>  emailCol;
//    @FXML  TableColumn<User, String>  firstNameCol;
//    @FXML  TableColumn<User, String>  lastNameCol;
//
//    ObservableList<User> userObserverableList = FXCollections.observableArrayList();
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            loadDataFromDB();
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        userScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//    }
//
//    void loadDataFromDB() throws SQLException{
//        loadUserFromDB();
//    }
//
//    public void loadUserFromDB() throws SQLException {
//        userObserverableList.clear();
//
//        Statement stm = DBConn.getConnection().createStatement();
//        ResultSet rs = stm.executeQuery("SELECT * FROM User ");
//
//        while(rs.next()){
//            String _username = rs.getString(1);
//            String _email = rs.getString(2);
//            String _firstname ="";
//            String _lastname = "";
//            try{
//                _firstname = rs.getString(3);
//                _lastname = rs.getString(4);
//            }catch (Exception e){
//                System.out.println(e);
//            }
//
//            userObserverableList.add(new User(_username, _email, _firstname, _lastname));
//        }
//
//        usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
//        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
//        firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
//        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
//
//        userTableView.setItems(userObserverableList);
//    }
//
//}

/**
 *@author neko941, klangthang
 *Created on: 2022-02-09
 *
 */

package Controllers;

import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Stream;


import static Controllers.ColorController.changeLabelText;
import static ExternalConnections.DBUtilities.*;


public class AdminController implements Initializable {
    @FXML private ScrollPane userScrollPane;
    @FXML  TableView <User> userTableView;
    @FXML  TableColumn<User, String>  usernameCol;
    @FXML  TableColumn<User, String>  emailCol;
    @FXML  TableColumn<User, String>  firstNameCol;
    @FXML  TableColumn<User, String>  lastNameCol;

    @FXML TextField FirstNameField;
    @FXML TextField LastNameField;
    @FXML TextField UserNameField;
    @FXML TextField EmailField;

    @FXML Label userNameWarning;
    @FXML Label emailWarning;

    @FXML Button CancelButton;

    static User selectedUser;

    ObservableList<User> userObservableList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param resourceBundle
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDataFromDB();
        } catch (SQLException e) {
            System.out.println(e);
        }
        userScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    void loadDataFromDB() throws SQLException{
        loadUserFromDB();
    }

    /**
     * Loads all users from database
     * @throws SQLException
     */

    public void loadUserFromDB() throws SQLException {
        userObservableList.clear();

        userObservableList.addAll(fetchAllUsersFromDatabase());


        usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
        userTableView.setItems(userObservableList);
    }


    @FXML
    private void LogoutButtonOnAction(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/UI/LoginUI.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setWidth(550);
            stage.setHeight(580);
            stage.show();
        } catch (Exception e) {
            System.err.printf("Error: %s%n", e.getMessage());
        }
    }


    /**
     * When user clicks on a row in the table, the selected user is set to the selectedUser variable
     *
     */

    public void adminUserTableOnMouseDoubleClicked() throws IOException, SQLException {
        //Get the user from the user table view
        selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            URL resourceUrl = getClass().getResource("/UI/admin_edit_popup.fxml");
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setController(this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            FirstNameField.setText(selectedUser.getFirstname());
            LastNameField.setText(selectedUser.getLastname());
            EmailField.setText(selectedUser.getEmail());
            UserNameField.setText(selectedUser.getUsername());
        }
    }


    public boolean checkUserNameAndPasswordAvailable()
    {
        return Stream.of(
                        // check username
                        selectedUser.getUsername().equals(UserNameField.getText()) || changeLabelText(
                                isUsernameAvailable(UserNameField.getText()),
                                UserNameField.getText().isBlank(),
                                userNameWarning,
                                "",
                                "Username already exists"),
                        // check email
                        selectedUser.getEmail().equals(EmailField.getText()) || changeLabelText(
                                isEmailAvailable(EmailField.getText()),
                                EmailField.getText().isBlank(),
                                emailWarning,
                                "",
                                "Email already exists"))
                .allMatch(val -> val);
    }


    @FXML
    public void CloseButtonOnAction(ActionEvent event){
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Used when the admin wants to save the user profile that admin has changed
     *
     * @param event when user clicks on "Save" button
     */
    @FXML
    public void SaveButtonOnActon(ActionEvent event) throws SQLException {
        if(checkUserNameAndPasswordAvailable())
        {
            User temp = fetchUser(selectedUser.getUsername());
            temp.setEmail(EmailField.getText());
            temp.setUsername(UserNameField.getText());
            temp.setFirstname(FirstNameField.getText());
            temp.setLastname(LastNameField.getText());

            editUser(temp);
            CloseButtonOnAction(new ActionEvent());
            loadDataFromDB();
        }
    }

    /**
     * Used when the admin wants to delete the user profile
     *
     * @param event when user clicks on "Delete" button
     */
    @FXML
    public void DeleteButtonOnButton(ActionEvent event) throws SQLException {
        deleteUser(fetchUser(selectedUser.getUsername()));
        CloseButtonOnAction(new ActionEvent());
        loadDataFromDB();
    }
}
