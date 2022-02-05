/**
 * @author jatenderjossan, neko941
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.event.EventListenerSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

public class DBUtilities {

    // Connection to the database
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    // Queries for the database
    private static final String INSERT_NEW_USER_QUERY = "INSERT INTO User (firstName, lastName, userName, password, email) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_EVENT_QUERY = "INSERT INTO Event (eventName, eventDate, eventTime, duration, location, reminder, priority, emails) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_LOCATION_QUERY = "INSERT INTO Location (street, houseNumber, zip, city, country, building, room) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_ATTACHMENT_QUERY = "INSERT INTO Attachments (fileName, file, eventID) VALUES (?, ?, ?)";
    private static final String MAKE_USER_EVENT_TABLE_QUERY = "INSERT INTO User_Event (eventID, userID) VALUES (?, ?)";

    private static final String EDIT_USER_QUERY = "UPDATE User SET firstname = ?, lastname = ?, username = ?, email = ? WHERE userID = ?";
    private static final String EDIT_EVENT_QUERY = "UPDATE Event SET eventName = ?, eventDate = ?, eventTime = ?, duration = ?, location = ?, priority = ?, reminder = ? , emails = ? WHERE eventID = ?";
    private static final String EDIT_LOCATION_QUERY = "UPDATE Location SET street = ?, houseNumber = ?, zip = ?, city = ?, country = ?, building = ?, room = ? WHERE locationID = ?";

    private static String VERIFY_USER_QUERY;
    private static final String EMAIL_AVAILABLE_QUERY = "SELECT * FROM User WHERE email = ?";
    private static final String USERNAME_AVAILABLE_QUERY = "SELECT * FROM User WHERE username = ?";

    private static final String DELETE_EVENT_QUERY = "DELETE FROM Event WHERE eventID = ?";
    private static final String DELETE_ATTACHMENT_QUERY = "DELETE FROM Attachment WHERE eventID = ?";
    private static final String DELETE_USER_EVENT_BRIDGE_QUERY = "DELETE FROM User_Event WHERE userID = ? AND eventID = ?";

    private static String GET_USER_QUERY;
    private static final String GET_ALL_EVENTS_FROM_USER_QUERY = "SELECT * FROM Event WHERE emails LIKE ?";
    // TODO: Uncomment this when the bridge works
    // private static final String GET_ALL_EVENTS_FROM_USER_QUERY = "SELECT * FROM Event WHERE User_Event.userID = ? AND User_Event.eventID = Event.eventID";
    private static final String GET_EVENT_FROM_ID = "SELECT * FROM Event WHERE eventID = ?";
    private static final String GET_LOCATION_FROM_EVENT_QUERY = "SELECT * FROM Location WHERE locationID = ?";
    private static final String GET_ATTACHMENTS_FROM_EVENT_QUERY = "SELECT * FROM Attachments WHERE eventID = ?";
    private static final String GET_PARTICIPANTS_FROM_EVENT_QUERY = "SELECT * FROM Participants WHERE Participants.eventID = ?";

    //##########################################################################################
    //                                         Methods
    //##########################################################################################

    /**
     * Opens a connection to the database if no connection is existing.
     *      If a connection is existing, this connection is closed
     */
    public static void DBUtilities(){
        connection = DBConn.getConnection();
    }

    //##########################################################################################

    /**
     * Makes a new entry for a new user in the database when a new user registers.
     * Returns ID of the user on successful insertion
     *
     * @param user makes an entry for the user which should be added to the database
     * @return -1 on unsuccessful insertion
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
     * @param event event which should be saved
     * @return -1 on unsuccessful insertion
     */
    public static int insertNewEvent(Event event) {
        int key = -1;

        try {
            int locationID = insertNewLocation(event.getLocation());
            preparedStatement = connection.prepareStatement(INSERT_NEW_EVENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, event.getEventName());
            preparedStatement.setDate(2, Date.valueOf(event.getDate()));
            preparedStatement.setTime(3, Time.valueOf(event.getTime()));
            preparedStatement.setInt(4, event.getDuration());
            preparedStatement.setInt(5, locationID);
            preparedStatement.setString(6, event.getReminder().name());
            preparedStatement.setString(7, event.getPriority().name());
            //TODO: remove this
            String result = StringUtils.join(event.getEmails(), ",");
            preparedStatement.setString(8, result);
            preparedStatement.executeUpdate();
            // get the ID of the event from the database
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("No eventID received");
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
     * @param location the location which should be saved in the database
     * @return -1 on unsuccessful insertion
     */
    private static int insertNewLocation (Location location) {
        PreparedStatement insertLocationPreparedStatement = null;
        int key = -1;
            
        try {
            insertLocationPreparedStatement = connection.prepareStatement(INSERT_NEW_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertLocationPreparedStatement.setString(1, location.getStreet());
            insertLocationPreparedStatement.setInt(2, location.getStreetNumber());
            insertLocationPreparedStatement.setString(3, location.getZip());
            insertLocationPreparedStatement.setString(4, location.getCity());
            insertLocationPreparedStatement.setString(5, location.getCountry());
            insertLocationPreparedStatement.setInt(6, location.getBuilding());
            insertLocationPreparedStatement.setInt(7, location.getRoom());
            insertLocationPreparedStatement.executeUpdate();

            // get the ID of the location from the database
            resultSet = insertLocationPreparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("could not get the key");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return key;
    }

    /**
     * Inserts a new file into the database.
     * Return ID on successful insertion
     *
     * @param event the event to which the file belongs
     * @param file the file which should be inserted into the database
     * @return -1 on unseccssful insertion
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
     * @param userID ID of the user which creates an event
     * @param eventID ID of the event to which the user os going to go
     * @return true on successful connection of user to the event
     */
    public static boolean createUser_EventBridge(final int userID, final int eventID) {
        boolean created = false;
            
        try {
            preparedStatement = connection.prepareStatement(MAKE_USER_EVENT_TABLE_QUERY);
            preparedStatement.setInt(1, eventID);
            preparedStatement.setInt(2, userID);
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
     * @param user user, which wants to update his credentials
     * @return false on unsuccessful editing
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
     * @param event the event which should be updated
     * @return true on successful editing
     */
    public static boolean editEvent(Event event) {
        boolean edited = false;
            
        try {
            //TODO: confirm with Jatender if the next line makes sense
            int locationID = insertNewLocation(event.getLocation());
            preparedStatement = connection.prepareStatement(EDIT_EVENT_QUERY);
            preparedStatement.setString(1, event.getEventName());
            preparedStatement.setDate(2, Date.valueOf(event.getDate()));
            preparedStatement.setTime(3, Time.valueOf(event.getTime()));
            preparedStatement.setInt(4, event.getDuration());
            preparedStatement.setInt(5, locationID);
            preparedStatement.setString(6, event.getPriority().name());
            preparedStatement.setString(7, event.getReminder().name());
            String result = StringUtils.join(event.getEmails(), ",");
            preparedStatement.setString(8, result);
            preparedStatement.setInt(9, event.getEventID());
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
     * @param location the location which should be updated in the database
     * @return true on successful editing
     */
    public static boolean editLocation(Location location) {
        boolean edited = false;

        try {
            preparedStatement = connection.prepareStatement(EDIT_LOCATION_QUERY);
            preparedStatement.setString(1, location.getStreet());
            preparedStatement.setInt(2, location.getStreetNumber());
            preparedStatement.setString(3, location.getZip());
            preparedStatement.setString(4, location.getCity());
            preparedStatement.setString(5, location.getCountry());
            preparedStatement.setInt(6, location.getBuilding());
            preparedStatement.setInt(7, location.getRoom());
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
     * @param credential the email or username of the user which should be verified
     * @return true on successful verification
     */
    public static boolean verifyUser (final String credential, final String password) {
        boolean verified = false;

        // first we check if the given credential is a username or an email of the user
        // and we prepare the query according to that
        boolean containsAt = credential.contains("@");
        if (containsAt) {
            VERIFY_USER_QUERY = "SELECT * FROM User WHERE email = ? AND password = ?";
        } else {
            VERIFY_USER_QUERY = "SELECT * FROM User WHERE username = ? AND password = ?";
        }

        // password encryption
        String encryptedPassword = Security.sha512(password);

        try {
            // then we prepare the preparedStatement according to that
            preparedStatement = connection.prepareStatement(VERIFY_USER_QUERY);
            preparedStatement.setString(1, credential);
            preparedStatement.setString(2, encryptedPassword);
            resultSet = preparedStatement.executeQuery();

            if (containsAt) {
                // if we are searching with the email of the user
                verified = verify(resultSet, "email", credential, encryptedPassword);
            } else {
                // if we are searching with the username of the user
                verified = verify(resultSet, "username", credential, encryptedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closePreparedStatement();
        }

        return verified;
    }

    /**
     * Checks if a given email is available or not
     *
     * @param email email of the user
     * @return true if email is available
     */
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

    /**
     * Checks if a given username is available
     *
     * @param username username of the user
     * @return true if the username is available
     */
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
     * @param eventID the ID of the event which should be deleted
     * @return true on successful deletion
     */
    public static boolean deleteEvent(final int eventID) {
        boolean deletedEvent = false;

        try {
            preparedStatement = connection.prepareStatement(DELETE_EVENT_QUERY);
            preparedStatement.setInt(1, eventID);
            preparedStatement.executeUpdate();

            deletedEvent = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return deletedEvent;
    }

    /**
     * Deletes all references from an event for a user from the database.
     *
     * @param userID ID of the user
     * @param eventID ID of the event
     * @return true on successful deletion
     */
    public static boolean deleteUser_EventBridge(final int userID, final int eventID) {
        boolean deletedBridge = false;

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER_EVENT_BRIDGE_QUERY);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, eventID);
            preparedStatement.executeUpdate();

            deletedBridge = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
        return deletedBridge;
    }

    /**
     * Deletes a file from the database when an event is which includes an attachment
     *      or when the attachment is deleted by user
     *
     * @param eventID ID of the event
     * @return true on successful deletion
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

    //##########################################################################################

    /**
     * This function is for fetching a user.
     *
     * @param credential email or username of the user
     * @return user - the user we looked for with the credential
     */
    public static User fetchUser(final String credential) {
        User user = null;

        // first we check if the given credential is the username or the email of the user
        // and prepare the query according to it
        boolean containsAt = credential.contains("@");
        if (containsAt) {
            GET_USER_QUERY = "SELECT * FROM User WHERE email = ?";
        } else {
            GET_USER_QUERY = "SELECT * FROM User WHERE username = ?";
        }

        try {
            // then we set the email or the username of the user in the placeholder
            preparedStatement = connection.prepareStatement(GET_USER_QUERY);
            preparedStatement.setString(1, credential);
            resultSet = preparedStatement.executeQuery();
            if (containsAt) {
                // if we are using the email of the user for searching the user in the table
                user = getUser(resultSet, "email", credential);
            } else {
                // if we are using the username of the user for searching the user in the table
                user = getUser(resultSet, "username", credential);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }

        return user;
    }

    /**
     * Fetches all events from a given user.
     *
     * @param user the user, from which we want all the events
     * @return arraylist with all events a user is participating
     */
    public static ArrayList<Event> fetchAllEventsFromUser(final User user) {
        ArrayList<Event> events = new ArrayList<>();
            
        try {
            //TODO: remove this after the bridge works
            preparedStatement = connection.prepareStatement(GET_ALL_EVENTS_FROM_USER_QUERY);
            preparedStatement.setString(1, "%" + user.getEmail() + "%");
//            preparedStatement = connection.prepareStatement(GET_ALL_EVENTS_FROM_USER_QUERY);
//            preparedStatement.setInt(1, user.getId());
            resultSet = preparedStatement.executeQuery();

//            int size = 0;
//            if (resultSet != null)
//            {
//                resultSet.last();    // moves cursor to the last row
//                size = resultSet.getRow(); // get row id
//            }
//            System.out.println(size);
            while (resultSet.next()) {
                // primary key of the entity "event"
                int eventID = resultSet.getInt("eventID");
                String eventName = resultSet.getString("eventName");
                LocalDate eventDate = resultSet.getDate("eventDate").toLocalDate();
                LocalTime eventTime = resultSet.getTime("eventTime").toLocalTime();
                Reminder reminder = Enum.valueOf(Reminder.class, resultSet.getString("reminder"));
                Priority priority = Enum.valueOf(Priority.class, resultSet.getString("priority"));
                //TODO: remove this
                String[] emails = resultSet.getString("emails").split(",");
                int duration = resultSet.getInt("duration");
                // argument - foreign key of the location table
                Location location = fetchLocationFromEvent(resultSet.getInt("location"));
                // argument - primary key of the event
                ArrayList<User> participants = fetchParticipants(eventID);
                // argument - primary key of the event
                ArrayList<File> attachments = fetchAttachments(eventID);

                events.add(new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, emails, attachments, reminder, priority));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return events;
    }

    public static ArrayList<Event> fetchAllEventsWithReminderFromDatabase() {
        ArrayList<Event> events = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM Event");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // primary key of the entity "event"
                int eventID = resultSet.getInt("eventID");
                String eventName = resultSet.getString("eventName");
                LocalDate eventDate = resultSet.getDate("eventDate").toLocalDate();
                LocalTime eventTime = resultSet.getTime("eventTime").toLocalTime();
                Reminder reminder = Enum.valueOf(Reminder.class, resultSet.getString("reminder"));
                Priority priority = Enum.valueOf(Priority.class, resultSet.getString("priority"));
                //TODO: remove this
                String[] emails = resultSet.getString("emails").split(",");
                int duration = resultSet.getInt("duration");
                // argument - foreign key of the location table
                Location location = fetchLocationFromEvent(resultSet.getInt("location"));
                // argument - primary key of the event
                ArrayList<User> participants = fetchParticipants(eventID);
                // argument - primary key of the event
                ArrayList<File> attachments = fetchAttachments(eventID);

                LocalDateTime reminderTime = reminder.getReminderTime(LocalDateTime.of(eventDate, eventTime));
                events.add(new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, emails, attachments, reminder, priority, reminderTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }

        Collections.sort(events);
        return events;
    }

    public static Event fetchEventsFromID(final int eventID) {

        try {
            preparedStatement = connection.prepareStatement(GET_EVENT_FROM_ID, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, eventID);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            // primary key of the entity "event"
            String eventName = resultSet.getString("eventName");
            LocalDate eventDate = resultSet.getDate("eventDate").toLocalDate();
            LocalTime eventTime = resultSet.getTime("eventTime").toLocalTime();
            Reminder reminder = Enum.valueOf(Reminder.class, resultSet.getString("reminder"));
            Priority priority = Enum.valueOf(Priority.class, resultSet.getString("priority"));
            //TODO: remove this
            String[] emails = resultSet.getString("emails").split(",");
            int duration = resultSet.getInt("duration");
            // argument - foreign key of the location table
            Location location = fetchLocationFromEvent(resultSet.getInt("location"));
            // argument - primary key of the event
            ArrayList<User> participants = fetchParticipants(eventID);
            // argument - primary key of the event
            ArrayList<File> attachments = fetchAttachments(eventID);

            return new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, emails, attachments, reminder, priority);

        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
            closeResultSet();
        }
        return null;
    }

    //#####################################################################################
    //
    //                                  SUBROUTINES
    //
    //#####################################################################################

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

    /**
     * This method fetches the location where the event is going to take place.
     *
     * @param locationID ID of the location (this is the foreign key if the entity "event")
     * @return object with the location details
     */
    private static Location fetchLocationFromEvent (final int locationID) {
        PreparedStatement fetchLocationStatement = null;
        ResultSet fetchLocationResultSet;

        try {
            fetchLocationStatement = connection.prepareStatement(GET_LOCATION_FROM_EVENT_QUERY);
            fetchLocationStatement.setInt(1, locationID);
            fetchLocationResultSet = fetchLocationStatement.executeQuery();
            if (fetchLocationResultSet.next()) {
                String street = fetchLocationResultSet.getString(2);
                int houseNumber = fetchLocationResultSet.getInt(3);
                String zip = fetchLocationResultSet.getString(4);
                String city = fetchLocationResultSet.getString(5);
                String country = fetchLocationResultSet.getString(6);
                int building = fetchLocationResultSet.getInt(7);
                int room = fetchLocationResultSet.getInt(8);

                return new Location(street, houseNumber, zip, city, country, building, room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fetchLocationStatement != null;
                if (!fetchLocationStatement.isClosed()) {
                    fetchLocationStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method fetches all the participants of an event.
     *
     * @param eventID ID of the event (primary key of the entity "Event")
     * @return participants - returns a list of participants which participate in the event with the
     *      given eventID
     */
    private static ArrayList<User> fetchParticipants (final int eventID) {
        PreparedStatement fetchParticipantsPreparedStatement = null;
        ResultSet fetchParticipantsResultSet;
        ArrayList<User> participants = new ArrayList<>();

        try {
            fetchParticipantsPreparedStatement = connection.prepareStatement(GET_PARTICIPANTS_FROM_EVENT_QUERY);
            fetchParticipantsPreparedStatement.setInt(1, eventID);
            fetchParticipantsResultSet = fetchParticipantsPreparedStatement.executeQuery();
            while (fetchParticipantsResultSet.next()) {
                String username = fetchParticipantsResultSet.getString("username");
                String email = fetchParticipantsResultSet.getString("email");
                int userID = fetchParticipantsResultSet.getInt("userID");

                participants.add(new User(username, email, userID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fetchParticipantsPreparedStatement != null;
                if (!fetchParticipantsPreparedStatement.isClosed()) {
                    fetchParticipantsPreparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return participants;
    }

    /**
     * This method fetches all attachments for an event.
     *
     * @param eventID ID of the event
     * @return returns a list with all the files which are attached to the given event
     */
    private static ArrayList<File> fetchAttachments (final int eventID) {
        PreparedStatement fetchAttachmentPreparedStatement = null;
        ResultSet fetchAttachmentResultSet;
        ArrayList<File> attachments = new ArrayList<>();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        File file;

        try {
            fetchAttachmentPreparedStatement = connection.prepareStatement(GET_ATTACHMENTS_FROM_EVENT_QUERY);
            fetchAttachmentPreparedStatement.setInt(1, eventID);
            fetchAttachmentResultSet = fetchAttachmentPreparedStatement.executeQuery();
            while (fetchAttachmentResultSet.next()) {
                file = new File(fetchAttachmentResultSet.getString("fileName"));
                outputStream = new FileOutputStream(file);
                inputStream = fetchAttachmentResultSet.getBinaryStream("file");
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) > 0) {
                    outputStream.write(buffer);
                }
                attachments.add(file);
            }
            return attachments;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            // closing input and output streams
            try {
                if (outputStream != null)
                    outputStream.close();

                if (inputStream != null)
                    inputStream.close();

                assert fetchAttachmentPreparedStatement != null;
                if (!fetchAttachmentPreparedStatement.isClosed())
                    fetchAttachmentPreparedStatement.close();

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return attachments;
    }

    /**
     * This method is only used for looping through the table of user and verifying a given user
     *
     * @param resultSet the resultSet with the execution of the preparedStatement
     * @param searchWith defines if we are using the email or the username for searching of the user
     * @param credential email or username of the user
     * @param password the password of the user
     * @return true if the user is in the table
     * @throws SQLException if something went wrong with the resultSet
     */
    private static boolean verify (ResultSet resultSet, String searchWith, String credential, String password) throws SQLException {
        boolean verified = false;

        // looping through the table and looking for a suiting user
        while (resultSet.next()) {
            if (resultSet.getString(searchWith).equals(credential)
                    && resultSet.getString("password").equals(password))
            {
                verified = true;
                break;
            }
        }

        return verified;
    }

    /**
     * This method is only used for getting the user from the database
     *
     * @param resultSet the resultSet with the execution of the preparedStatement
     * @param searchWith defines if we are using the email or the username for searching of the user
     * @param credential email or username of the user
     * @return user the user we were searching for
     * @throws SQLException if something went wrong with the resultSet
     */
    private static User getUser(ResultSet resultSet, String searchWith, String credential) throws SQLException {
        User user = null;

        while (resultSet.next()) {
            if (resultSet.getString(searchWith).equals(credential)) {
                int userID = resultSet.getInt("userID");
                String firstname = resultSet.getString("userName");
                String lastname = resultSet.getString("lastName");
                String username = resultSet.getString("userName");
                String email = resultSet.getString("email");
                user = new User(userID, firstname, lastname, username, email);

                break;
            }
        }

        return user;
    }

}