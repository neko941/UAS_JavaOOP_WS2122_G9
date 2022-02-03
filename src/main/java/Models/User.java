/**
 * @author neko941, jatenderjossan
 * Created on: 2021-12-13
 * This class provides model for a user
 */

package Models;

public class User
{
    private int userID;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;

    /**
     * Constructor for fetching a user from the database
     * 
     * @param userID - ID of user
     * @param firstName - firstname of user
     * @param lastName - lastname of user
     * @param userName - username of user
     * @param email - email of user
     */
    public User (int userID, String firstName, String lastName, String userName, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
    }

    /**
     * Constructor for creating a user
     *
     * @param firstName - firstname of user
     * @param lastName - lastname of user
     * @param userName - username of user
     * @param password - password of user
     * @param email - email of user
     */
    public User (String firstName, String lastName, String userName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
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
        this.userName = userName;
        this.email = email;
        this.userID = userID;
    }

    // getters and setters
    public String getUsername() {return userName;}
    public void setUsername(String userName) {this.userName = userName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstname() {return firstName;}
    public void setFirstname(String firstName) {this.firstName = firstName;}

    public String getLastname() { return lastName; }
    public void setLastname(String lastname) { this.lastName = lastname; }

    public int getId() {return userID;}
    public void setId(int userID) {this.userID = userID;}
}