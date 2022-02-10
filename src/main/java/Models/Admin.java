/**
 * @author neko941, jatenderjossan
 * Created on: 2021-12-13
 * This class provides model for a Admin
 */

package Models;

public class Admin
{
    private int AdminID;
    private String firstName;
    private String lastName;
    private String AdminName;
    private String password;
    private String email;

    /**
     * Constructor for fetching a Admin from the database
     * 
     * @param AdminID - ID of Admin
     * @param firstName - firstname of Admin
     * @param lastName - lastname of Admin
     * @param AdminName - Adminname of Admin
     * @param email - email of Admin
     */
    public Admin (int AdminID, String firstName, String lastName, String AdminName, String email) {
        this.AdminID = AdminID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.AdminName = AdminName;
        this.email = email;
    }

    /**
     * Constructor for creating a Admin
     *
     * @param firstName - firstname of Admin
     * @param lastName - lastname of Admin
     * @param AdminName - Adminname of Admin
     * @param password - password of Admin
     * @param email - email of Admin
     */
    public Admin (String firstName, String lastName, String AdminName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.AdminName = AdminName;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for fetching a Admin as a participant from database and adding it to the participants list
     *
     * @param AdminName - Adminname of the Admin
     * @param email - email of the Admin
     * @param AdminID - AdminID of the Admin
     */
    public Admin (String AdminName, String email, int AdminID) {
        this.AdminName = AdminName;
        this.email = email;
        this.AdminID = AdminID;
    }

    // getters and setters
    public String getAdminname() {return AdminName;}
    public void setAdminname(String AdminName) {this.AdminName = AdminName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstname() {return firstName;}
    public void setFirstname(String firstName) {this.firstName = firstName;}

    public String getLastname() { return lastName; }
    public void setLastname(String lastname) { this.lastName = lastname; }

    public int getId() {return AdminID;}
    public void setId(int AdminID) {this.AdminID = AdminID;}
}