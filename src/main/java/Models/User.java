/**
 * Author: neko941, jatenderjossan
 * Created on: 2021-12-13
 *
 * This class provides model for a user
 */

package Models;

public class User
{
    /**
     * @param email: unique
     * @param username: unique
     * @param password
     * @param firstName
     * @param lastName
     * @param userID: unique
     * @param antiPhishing: a string that user set
     */
    private int userID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;

    // constructor
    public User(String email, String username, String password, String firstName, String lastName, int userID)
    {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for creating a user
     *
     * @param firstName - firstname of user
     * @param lastName - lastname of user
     * @param username - username of user
     * @param password - password of user
     * @param email - email of user
     */
    public User (String firstName, String lastName, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for fetching a user as a participant from database and adding it to the participants list
     *
     * @param username - username of the user
     * @param email - email of the user
     * @param userID - userID of the user
     */
    public User (String username, String email, int userID) {
        this.username = username;
        this.email = email;
        this.userID = userID;
    }

    // getters and setters
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

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