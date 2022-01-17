/**
 * Author: neko941
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
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userID;
    private String antiPhishing;

    // constructor
    public User(String email, String username, String password, String firstName, String lastName, String userID, String antiPhishing)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.antiPhishing = antiPhishing;
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
    public void setLastname(String lastname) { this.lastName = lastName; }

    public String getId() {return userID;}
    public void setId(String userID) {this.userID = userID;}

    public String getAntiPhishing() {return antiPhishing;}
    public void setAntiPhishing(String antiPhishing) {this.antiPhishing = antiPhishing;}
}