/**
 * @author neko941, jatenderjossan
 * Created on: 2021-12-13
 * This class provides model for a user
 */

package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User
{
    private int userID;
    private String firstName;
    public String lastName;

    public String username;
    public String password;
    private String email;

    /**
     * Constructor for fetching a user from the database
     * 
     * @param userID - ID of user
     * @param firstname - firstname of user
     * @param lastName - lastname of user
     * @param userName - username of user
     * @param email - email of user
     */
    public User (int userID, String firstname, String lastName, String userName, String email) {
        this.userID = userID;
        this.firstName = firstname;
        this.lastName = lastName;
        this.username = userName;
        this.email = email;
    }

    /**
     * Constructor for creating a user
     *
     * @param firstname - firstname of user
     * @param lastName - lastname of user
     * @param userName - username of user
     * @param password - password of user
     * @param email - email of user
     */
    public User (String firstname, String lastName, String userName, String password, String email) {
        this.firstName = firstname;
        this.lastName = lastName;
        this.username = userName;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for fetching a user as a participant from database and adding it to the participants list
     *
     * @param userName - username of the user
     * @param email - email of the user
     * @param userID - userID of the user
     */
    public User (String userName, String email, int userID) {
        this.username = userName;
        this.email = email;
        this.userID = userID;
    }

    public User (String userName, String email, String firstname, String lastName)
    {
        this.username = userName;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastName;

    }

    public void printInfo()
    {
        System.out.println(this.getUsername());
        System.out.println(this.getEmail());
        System.out.println(this.getFirstname());
        System.out.println(this.getLastname());
        System.out.println();
    }



    // getters and setters
    public final String getUsername() {return username;}
    public void setUsername(String userName) {this.username = userName;}

    public final String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public final String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public final String getFirstname() {return firstName;}
    public void setFirstname(String firstName) {this.firstName = firstName;}

    public final String getLastname() { return lastName; }
    public void setLastname(String lastname) { this.lastName = lastname; }

    public int getId() {return userID;}
    public void setId(int userID) {this.userID = userID;}

    private final StringProperty userNameProp = new SimpleStringProperty();

    public final StringProperty getUserNamePropProperty() {
        return userNameProp;
    }
    public final String getUserNameProp() {
        return userNameProp.get();
    }

    public final void setUserNameProp(String value) {
        userNameProp.set(value);
    }
}