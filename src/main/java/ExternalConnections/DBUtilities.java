/**
 * Author: jatenderjossan
 * Created on: Dec. 23, 2021
 *
 * This class provides everything SQL related and manages for example fetching and adding data from and to the database
 */

package ExternalConnections;

import Controllers.Security;
import Models.Event;
import Models.Location;
import Models.Priority;
import Models.Reminder;
import Models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class DBUtilities {

    // For connection to the database
    private static final String DBUrl = "jdbc:mysql://bkyhmn5ukri7jfw1kpjs-mysql.services.clever-cloud.com:3306/bkyhmn5ukri7jfw1kpjs";
    private static final String DBUsername = "upqkamkqerixvpbb";
    private static final String DBPassword = "dNJnm1pH1qC7uSU2IgrJ";

    // Connection to the database
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    // Queries for the database
    private static final String INSERT_NEW_USER_QUERY = "INSERT INTO User (firstName, lastName, userName, password, email) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_EVENT_QUERY = "INSERT INTO Event (eventName, eventDate, eventTime, duration, location, reminder, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_LOCATION_QUERY = "INSERT INTO Location (street, houseNumber, zip, city, country, building, room) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_ATTACHMENT_QUERY = "INSERT INTO Attachment (fileName, file, fk_eventID) VALUES (?, ?, ?)";
    private static final String MAKE_USER_EVENT_TABLE_QUERY = "INSERT INTO User_Event (userID, eventID) VALUES (?, ?)";

    private static final String EDIT_USER_QUERY = "UPDATE User SET firstname = ?, lastname = ?, username = ?, email = ? WHERE userID = ?";
    private static final String EDIT_EVENT_QUERY = "UPDATE Event SET eventName = ?, eventDate = ?, eventTime = ?, duration = ?, location = ?, priority = ?, reminder = ? WHERE eventID = ?";
    private static final String EDIT_LOCATION_QUERY = "UPDATE Location SET street = ?, houseNumber = ?, zip = ?, city = ?, country = ?, building = ?, room = ? WHERE locationID = ?";

    private static final String VERIFY_USER_QUERY = "SELECT * FROM User WHERE username = ? AND password = ?";
    private static final String USER_AVAILABLE_QUERY = "SELECT * FROM User WHERE username = ? OR email = ?";
    private static final String EMAIL_AVAILABLE_QUERY = "SELECT * FROM User WHERE email = ?";
    private static final String USERNAME_AVAILABLE_QUERY = "SELECT * FROM User WHERE username = ?";

    private static final String DELETE_EVENT_QUERY = "DELETE FROM Event WHERE eventID = ?";
    private static final String DELETE_ATTACHMENT_QUERY = "DELETE FROM Attachment WHERE eventID = ?";
    private static final String DELETE_USER_EVENT_BRIDGE_QUERY = "DELETE FROM User_Event WHERE userID = ? AND eventID = ?";

    private static final String GET_ALL_EVENTS_FROM_USER_QUERY = "SELECT * FROM Event WHERE User_Event.userID = ? AND User_Event.eventID = Event.eventID";
    private static final String GET_LOCATION_FROM_EVENT_QUERY = "SELECT * FROM Location WHERE locationID = ?";
    private static final String GET_ATTACHMENTS_FROM_EVENT_QUERY = "SELECT * FROM Attachments WHERE";
    private static final String GET_PARTICIPANTS_FROM_EVENT_QUERY = "SELECT * FROM Participants WHERE Participants.eventID = ?";

    //##########################################################################################
    //                                         Methods
    //##########################################################################################

    /**
     * Opens a connection to the database if no connection is existing.
     *      If a connection is existing, this connection is closed
     */
    DBUtilities(){
            connection = DBConn.getConnection();
        }
        
        // private static void connectToDatabase() {
        //     try {
        //     
        //         connection = DriverManager.getConnection(DBUrl, DBUsername, DBPassword);
        //         System.out.println("Connected to database...");
        //     } catch (SQLException e) {
        //         e.printStackTrace();
        //         System.out.println("Error Code opening: " +e.getErrorCode());
        //         System.out.println("Error message closing: " +e.getMessage());
        //     }
        // }

//        public static void connectToDatabase()
//        {
//            String databaseUser = "root";
//            String databasePassword = "-KFyAH8cp99JYJr";
//            String url = "jdbc:mysql://127.0.0.1:3306/quartz" ;
//
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                connection = DriverManager.getConnection(url, databaseUser, databasePassword);
//
//            }catch(Exception e){
//                e.printStackTrace();
//                e.getCause();
//            }
//        }
        /**
         * Closes an existing connection to the database.
         * This function is used everytime a connection to the database is created to
         * avoid performance issues by the garbage collector
         */
        // private static void disconnectFromDatabase() {
        //     if (connection != null) {
        //         try {
        //             connection.close();
        //             if (connection.isClosed())
        //                 System.out.println("Disconnected from database...");
        //             connection = null;
        //         } catch (SQLException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }

    /**
     * This function closes an opened preparedStatement
     */
    private static void closePreparedStatement() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function closes an open resultSet
     */
    private static void closeResultSet() {
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //##########################################################################################

    /**
     * Makes a new entry for a new user in the database when a new user registers.
     * Returns ID of the user on successful insertion
     *
     * @param: user - makes an entry for the user which should be added to the database
     * @return: -1 on unsuccessful insertion
     */
    public static int insertNewUser(User user) {
        int key = -1;

        // password encryption
        String encryptedPassword = Security.sha512(user.getPassword());

        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, encryptedPassword);
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();

            // get the ID of the user from the database
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("No userID received");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }

        return key;
    }

    /**
     * Makes a new entry for a new event in the database.
     *
     * @param: event - event which should be saved
     * @return -1 on unsuccessful insertion
     */
    public static int insertNewEvent(Event event) {
        int key = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_EVENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, event.getEventName());
            preparedStatement.setDate(2, Date.valueOf(event.getDate()));
            preparedStatement.setTime(3, Time.valueOf(event.getTime()));
            preparedStatement.setInt(4, event.getDuration());
            int locationID = insertNewLocation(event.getLocation());
            preparedStatement.setInt(5, locationID);
            preparedStatement.setString(6, event.getReminder().name());
            preparedStatement.setString(7, event.getPriority().name());
            preparedStatement.executeUpdate();
            // get the ID of the event from the database
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("Could not get key");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return key;
    }

    /**
     * Makes an entry for a location in the database.
     * Return the ID on successful insertion
     *
     * @param: location - the location which should be saved in the database
     * @return: -1 on unsuccessful insertion
     */
    public static int insertNewLocation (Location location) {
        int key = -1;
            
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, location.getStreet());
            preparedStatement.setString(2, location.getStreetNumber());
            preparedStatement.setString(3, location.getZip());
            preparedStatement.setString(4, location.getCity());
            preparedStatement.setString(5, location.getCountry());
            preparedStatement.setString(6, location.getBuilding());
            preparedStatement.setString(7, location.getRoom());
            preparedStatement.executeUpdate();

            // get the ID of the location from the database
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("could not get the key");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return key;
    }

    /**
     * Inserts a new file into the database.
     * Return ID on successful insertion
     *
     * @param: event - the event to which the file belongs
     * @param: file - the file which should be inserted into the database
     * @return: -1 on unseccssful insertion
     */
    public static int insertNewAttachment(Event event, File file) {
        FileInputStream fileInputStream = null;
        int key = -1;
            
        try {
            fileInputStream = new FileInputStream(file);

            preparedStatement = connection.prepareStatement(INSERT_NEW_ATTACHMENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, file.getName());
            preparedStatement.setBinaryStream(2, fileInputStream);
            preparedStatement.setInt(3, event.getEventID());
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("ID not received");
            }
        } catch (SQLException | FileNotFoundException e){
            e.printStackTrace();
        } finally {
            // closing fileInputStream
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closePreparedStatement();
            closeResultSet();
        }
        return key;
    }

    /**
     * This method should be called everytime a user creates an event for making a
     *      reference in the database, so that one knows which user is going to go to which event.
     *
     * @param: userID - ID of the user which creates an event
     * @param: eventID - ID of the event to which the user os going to go
     * @return: true on successful connection of user to the event
     */
    public static boolean createUser_EventBridge(final int userID, final int eventID) {
        boolean created = false;
            
        try {
            preparedStatement = connection.prepareStatement(MAKE_USER_EVENT_TABLE_QUERY);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, eventID);
            preparedStatement.executeUpdate();

            created = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return created;
    }

    //##########################################################################################

    /**
     * Updates user information in the database.
     *
     * @param: user - user, which wants to update his credentials
     * @return: false on unsuccessful editing
     */
    public static boolean editUser (User user){
        boolean edited = false;
            
        try {
            preparedStatement = connection.prepareStatement(EDIT_USER_QUERY);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();

            edited = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return edited;
    }

    /**
     * Updates an event in the database
     *
     * @param: event - the event which should be updated
     * @return: true on successful editing
     */
    public static boolean editEvent(Event event) {
        boolean edited = false;
            
        try {
            preparedStatement = connection.prepareStatement(EDIT_EVENT_QUERY);
            preparedStatement.setString(1, event.getEventName());
            preparedStatement.setDate(2, Date.valueOf(event.getDate()));
            preparedStatement.setTime(3, Time.valueOf(event.getTime()));
            preparedStatement.setInt(4, event.getDuration());
            preparedStatement.setInt(5, event.getLocation().getLocationID());
            preparedStatement.setString(6, event.getPriority().name());
            preparedStatement.setString(7, event.getReminder().name());
            preparedStatement.setInt(8, event.getEventID());
            preparedStatement.executeUpdate();

            edited = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return edited;
    }

    /**
     * Updated the location in the database, if the location changes
     *
     * @param: location - the location which should be updated in the database
     * @return: true on successful editing
     */
    public static boolean editLocation(Location location) {
        boolean edited = false;

        try {
            preparedStatement = connection.prepareStatement(EDIT_LOCATION_QUERY);
            preparedStatement.setString(1, location.getStreet());
            preparedStatement.setString(2, location.getStreetNumber());
            preparedStatement.setString(3, location.getZip());
            preparedStatement.setString(4, location.getCity());
            preparedStatement.setString(5, location.getCountry());
            preparedStatement.setString(6, location.getBuilding());
            preparedStatement.setString(7, location.getRoom());
            preparedStatement.setInt(8, location.getLocationID());
            preparedStatement.executeUpdate();

            edited = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return edited;
    }

    //##########################################################################################

    /**
     * Verifies if a given user corresponds to a user in the database.
     *
     * @param: username - username of the user
     * @param: password - password of the user
     * @return: true on successful verification
     */
    public static boolean verifyUser (final String username, final String password) {
        boolean verified = false;

        // password encryption
        String encryptedPassword = Security.sha512(password);
            
        try {
            preparedStatement = connection.prepareStatement(VERIFY_USER_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, encryptedPassword);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username) && resultSet.getString("password").equals(encryptedPassword)) {
                    verified = true;
                } else {
                    System.out.println("user not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return verified;
    }

    /**
     * Checks if a users username or email is available, since the username of all users should be unique
     *      Should be used before a new user is stored into the database.
     *
     * @param: user - user that should be checked for availability
     * @return: true if user is available
     */
    public static boolean isAvailable(User user) {
        boolean available = true;

        try {
            preparedStatement = connection.prepareStatement(USER_AVAILABLE_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(user.getUsername()) || resultSet.getString("email").equals(user.getEmail())) {
                    available = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            available = false;
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return available;
    }

    public static boolean isEmailAvailable(String email) {
        boolean available = true;

        try {
            preparedStatement = connection.prepareStatement(EMAIL_AVAILABLE_QUERY);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("email").equals(email)) {
                    available = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            available = false;
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return available;
    }

    public static boolean isUsernameAvailable(String username) {
        boolean available = true;

        try {
            preparedStatement = connection.prepareStatement(USERNAME_AVAILABLE_QUERY);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)) {
                    available = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            available = false;
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return available;
    }
    //##########################################################################################

    /**
     * Deletes a given event from database.
     *
     * @param eventID - the ID of the event which should be deleted
     * @return: true on successful deletion
     */
    public static boolean deleteEvent(final int eventID) {
        boolean deleted = false;

        try {
            preparedStatement = connection.prepareStatement(DELETE_EVENT_QUERY);
            preparedStatement.setInt(1, eventID);
            preparedStatement.executeUpdate();

            deleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return deleted;
    }

    /**
     * Deletes a file from the database when an event is which includes an attachment
     *      or when the attachment is deleted by user
     *
     * @param: eventID - ID of the event
     * @return: true on successful deletion
     */
    public static boolean deleteAttachment(final int eventID) {
        boolean deleted = false;
            
        try {
            preparedStatement = connection.prepareStatement(DELETE_ATTACHMENT_QUERY);
            preparedStatement.setInt(1, eventID);
            preparedStatement.executeUpdate();

            deleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return deleted;
    }

    /**
     * Deletes all references from an event for a user from the database.
     *
     * @param: userID - ID of the user
     * @param: eventID - ID of the event
     * @return: true on successful deletion
     */
    public static boolean deleteUser_EventBridge(final int userID, final int eventID) {
        boolean deleted = false;

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER_EVENT_BRIDGE_QUERY);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, eventID);
            preparedStatement.executeUpdate();

            deleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return deleted;
    }

    //##########################################################################################

    /**
     * Fetches all events from a given user.
     *
     * @param: user - the user, from which we want all the events
     * @return: arraylist with all events a user is participating
     */
    public static ArrayList<Event> fetchAllEventsFromUser (final User user) {
        ArrayList<Event> events = new ArrayList<>();
            
        try {
            preparedStatement = connection.prepareStatement(GET_ALL_EVENTS_FROM_USER_QUERY);
            preparedStatement.setInt(1, user.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // primary key of the entity "event"
                int eventID = resultSet.getInt("eventID");
                String eventName = resultSet.getString("eventName");
                LocalDate eventDate = resultSet.getDate("eventDate").toLocalDate();
                LocalTime eventTime = resultSet.getTime("eventTime").toLocalTime();
                int duration = resultSet.getInt("duration");
                // argument - foreign key of the location table
                Location location = fetchLocationFromEvent(resultSet.getInt("location"));
                // argument - primary key of the event
                ArrayList<User> participants = fetchParticipants(eventID);
                // argument - primary key of the event
                ArrayList<File> attachments = fetchAttachments(eventID);
                Reminder reminder = Enum.valueOf(Reminder.class, resultSet.getString("reminder"));
                Priority priority = Enum.valueOf(Priority.class, resultSet.getString("priority"));

                events.add(new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, attachments, reminder, priority));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return events;
    }

    /**
     * This method fetches the location where the event is going to take place.
     *
     * @param: locationID - ID of the location (this is the foreign key if the entity "event")
     * @return: location - returns the location corresponding to the given eventID
     */
    public static Location fetchLocationFromEvent (final int locationID) {
            
        try {
            preparedStatement = connection.prepareStatement(GET_LOCATION_FROM_EVENT_QUERY);
            preparedStatement.setInt(1, locationID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String street = resultSet.getString(2);
                String houseNumber = resultSet.getString(3);
                String zip = resultSet.getString(4);
                String city = resultSet.getString(5);
                String country = resultSet.getString(6);
                String building = resultSet.getString(7);
                String room = resultSet.getString(8);

                return new Location(street, houseNumber, zip, city, country, building, room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return null;
    }

    /**
     * This method fetches all the participants of an event.
     *
     * @param eventID - ID of the event (primary key of the entity "Event")
     * @return participants - returns a list of participants which participate in the event with the
     *      given eventID
     */
    public static ArrayList<User> fetchParticipants (final int eventID) {
        ArrayList<User> participants = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(GET_PARTICIPANTS_FROM_EVENT_QUERY);
            preparedStatement.setInt(1, eventID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                int userID = resultSet.getInt("userID");

                participants.add(new User(username, email, userID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return participants;
    }

    /**
     * This method fetches all attachments for an event.
     *
     * @param eventID - ID of the event
     * @return attachments - returns a list with all the files which are attached to an event
     */
    public static ArrayList<File> fetchAttachments (final int eventID) {
        ArrayList<File> attachments = new ArrayList<>();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        File file;

        try {
            preparedStatement = connection.prepareStatement(GET_ATTACHMENTS_FROM_EVENT_QUERY);
            preparedStatement.setInt(1, eventID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                file = new File(resultSet.getString("fileName"));
                outputStream = new FileOutputStream(file);
                inputStream = resultSet.getBinaryStream("file");
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) > 0) {
                    outputStream.write(buffer);
                }
                attachments.add(file);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
            // closing input and output streams
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return attachments;
    }

}