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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static Controllers.Debugging.printNotificationInConsole;

public class DBUtilities {

    // Connection to the database
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    // Queries for the database
    private static final String INSERT_NEW_USER_QUERY = "INSERT INTO User (firstName, lastName, userName, password, email) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_EVENT_QUERY = "INSERT INTO Event (eventName, eventDate, eventTime, duration, location, reminder, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_LOCATION_QUERY = "INSERT INTO Location (street, houseNumber, zip, city, country, building, room) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_NEW_ATTACHMENT_QUERY = "INSERT INTO Attachments (fileName, file, eventID) VALUES (?, ?, ?)";
    private static final String INSERT_NEW_PARTICIPANTS_QUERY = "INSERT INTO Participants (username, email, userID, eventID) VALUES (?, ?, ?, ?)";
    private static final String MAKE_USER_EVENT_TABLE_QUERY = "INSERT INTO User_Event (eventID, userID) VALUES (?, ?)";

    private static final String EDIT_USER_QUERY = "UPDATE User SET firstname = ?, lastname = ?, username = ?, email = ? WHERE userID = ?";
    private static final String EDIT_EVENT_QUERY = "UPDATE Event SET eventName = ?, eventDate = ?, eventTime = ?, duration = ?, location = ?, priority = ?, reminder = ? WHERE eventID = ?";
    private static final String EDIT_LOCATION_QUERY = "UPDATE Location SET street = ?, houseNumber = ?, zip = ?, city = ?, country = ?, building = ?, room = ? WHERE locationID = ?";

    private static String VERIFY_USER_QUERY;
    private static final String EMAIL_AVAILABLE_QUERY = "SELECT * FROM User WHERE email = ?";
    private static final String USERNAME_AVAILABLE_QUERY = "SELECT * FROM User WHERE username = ?";

    private static final String DELETE_EVENT_QUERY = "DELETE FROM Event WHERE eventID = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM User WHERE userID = ?";
    private static final String DELETE_ATTACHMENTS_QUERY = "DELETE FROM Attachments WHERE eventID = ?";
    private static final String DELETE_LOCATION_QUERY = "DELETE FROM Location WHERE locationID = ?";
    private static final String DELETE_USER_EVENT_BRIDGE_QUERY = "DELETE FROM User_Event WHERE userID = ? AND eventID = ?";
    private static final String DELETE_PARTICIPANTS_QUERY = "DELETE FROM Participants WHERE eventID = ?";

    private static String GET_USER_QUERY;
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM User";
    private static final String GET_ALL_EVENTS_WITH_REMINDER_QUERY = "SELECT * FROM Event";
    private static final String GET_ALL_EVENTS_FROM_USER_QUERY = "SELECT * FROM Participants WHERE userID = ?";
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
     */
    public static void insertNewUser(User user) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
    }

    /**
     * Makes a new entry for a new event in the database.
     *
     * @param user the user which created the event for the bridge
     * @param event event which should be saved
     * @return -1 on unsuccessful insertion
     */
    public static int insertNewEvent(User user, Event event) {
        int key = -1;   // this is the eventID

        try {
            // first we create a new event entry in the database
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

            resultSet = preparedStatement.getGeneratedKeys();   // get the ID of the event from the database
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            } else {
                throw new SQLException("No eventID received");
            }

            // then we create a bridge between the user and the event and assign the userID of the user
            // who created the event the eventID
            createUser_EventBridge(user.getId(), key);

            // we check if the user attached any attachments to the event
            if (event.getAttachments() != null || event.getAttachments().size() == 0) {
                // if so, we insert that into the database
                insertNewAttachments(key, event.getAttachments());
            }

            // we check if the user invited any participants to the event
            if (event.getParticipants() != null || event.getParticipants().size() != 0) {
                // if so we insert them into the database
                insertNewParticipants(key, event.getParticipants());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }

        return key;
    }

    //##########################################################################################

    /**
     * Updates user information in the database.
     *
     * @param user user, which wants to update his credentials
     */
    public static void editUser (User user){

        try {
            preparedStatement = connection.prepareStatement(EDIT_USER_QUERY);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();

            printNotificationInConsole(String.format("Information of user ID = %s is edited", user.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
    }

    /**
     * Updates an event in the database. Updates the corresponding location too.
     *
     * @param event the event which should be updated
     * @return true on successful editing
     */
    public static boolean editEvent(Event event) {
        boolean edited = false;

        try {
            editLocation(event.getLocation());
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

            // we check if the user attached any participants to the event
            if (event.getParticipants() != null || event.getParticipants().size() != 0) {
                // if so, we first delete the participants which were saved before
                deleteParticipants(event.getEventID());
                // and insert the new ones
                insertNewParticipants(event.getEventID(), event.getParticipants());
            }

            // we check if the user attached any attachments to the event
            if (event.getAttachments() != null || event.getAttachments().size() == 0) {
                // if so, we first delete the attachments which were saved before
                deleteAttachments(event.getEventID());
                // and insert the new ones
                insertNewAttachments(event.getEventID(), event.getAttachments());
            }

            printNotificationInConsole(String.format("Event \"%s\" is edited in the database", event.getEventName()));

            edited = true;
        } catch (SQLException | IOException e) {
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
    public static boolean isEmailAvailable(final String email) {
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
        }
        return available;
    }

    /**
     * Checks if a given username is available
     *
     * @param username username of the user
     * @return true if the username is available
     */
    public static boolean isUsernameAvailable(final String username) {
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
        }
        return available;
    }
    //##########################################################################################

    /**
     * Deletes a given event from database.
     *
     * @param event the event which should be deleted
     * @param user the user who is initiating it
     */
    public static void deleteEvent(User user, Event event) {

        try {
            // first we delete the event the user wants to delete
            preparedStatement = connection.prepareStatement(DELETE_EVENT_QUERY);
            preparedStatement.setInt(1, event.getEventID());
            preparedStatement. executeUpdate();

            // then we first delete the corresponding bridge
            deleteUser_EventBridge(user.getId(), event.getEventID());
            // then we secondly delete the attachments for that event
            deleteAttachments(event.getEventID());
            // then thirdly the corresponding location
            deleteLocation(event.getLocation().getLocationID());
            // and last but not least the participants of that event
            deleteParticipants(event.getEventID());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
    }

    public static void deleteUser(User user) {

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();

            printNotificationInConsole(String.format("User ID = %s is deleted", user.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }
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
            preparedStatement = connection.prepareStatement(GET_ALL_EVENTS_FROM_USER_QUERY);
            preparedStatement.setInt(1, user.getId());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int eventID = resultSet.getInt("eventID");
                Event event = fetchEventsFromID(eventID);
                events.add(event);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Fetching all events that need to send remind email
     *
     * @return list of events that need to send remind email
     */
    public static ArrayList<Event> fetchAllEventsWithReminderFromDatabase() {
        ArrayList<Event> events = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(GET_ALL_EVENTS_WITH_REMINDER_QUERY);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reminder reminder = Enum.valueOf(Reminder.class, resultSet.getString("reminder"));
                ArrayList<String> fetchedEmails = new ArrayList<>();
                if (reminder == Reminder.NO_REMINDER)
                {
                    continue;
                }
                // primary key of the entity "event"
                int eventID = resultSet.getInt("eventID");
                String eventName = resultSet.getString("eventName");
                LocalDate eventDate = resultSet.getDate("eventDate").toLocalDate();
                LocalTime eventTime = resultSet.getTime("eventTime").toLocalTime();
                Priority priority = Enum.valueOf(Priority.class, resultSet.getString("priority"));
                int duration = resultSet.getInt("duration");
                // argument - foreign key of the location table
                Location location = fetchLocationFromEvent(resultSet.getInt("location"));
                // argument - primary key of the event
                ArrayList<User> participants = fetchParticipants(eventID);
                // argument - primary key of the event
                ArrayList<File> attachments = fetchAttachments(eventID);
                for (User participant : participants){
                    fetchedEmails.add(participant.getEmail());
                }
                String[] emails = fetchedEmails.toArray(new String[0]);

                LocalDateTime reminderTime = reminder.getReminderTime(LocalDateTime.of(eventDate, eventTime));
                events.add(new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, emails, attachments, reminder, priority, reminderTime));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }

        Collections.sort(events);
        return events;
    }

    public static ArrayList<User> fetchAllUsersFromDatabase()  {
        ArrayList<User> users = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(GET_ALL_USERS_QUERY);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                users.add(new User(
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement();
        }

        return users;
    }

    public static Event fetchEventsFromID(final int eventID) {
        PreparedStatement fetchEventPreparedStatement;
        try {
            ArrayList<String> fetchedEmails = new ArrayList<>();
            fetchEventPreparedStatement = connection.prepareStatement(GET_EVENT_FROM_ID);
            fetchEventPreparedStatement.setInt(1, eventID);
            ResultSet eventResultSet = fetchEventPreparedStatement.executeQuery();

            eventResultSet.next();
            // primary key of the entity "event"
            String eventName = eventResultSet.getString("eventName");
            LocalDate eventDate = eventResultSet.getDate("eventDate").toLocalDate();
            LocalTime eventTime = eventResultSet.getTime("eventTime").toLocalTime();
            Reminder reminder = Enum.valueOf(Reminder.class, eventResultSet.getString("reminder"));
            Priority priority = Enum.valueOf(Priority.class, eventResultSet.getString("priority"));
            int duration = eventResultSet.getInt("duration");
            // argument - foreign key of the location table
            Location location = fetchLocationFromEvent(eventResultSet.getInt("location"));
            // argument - primary key of the event
            ArrayList<User> participants = fetchParticipants(eventID);
            // argument - primary key of the event
            ArrayList<File> attachments = fetchAttachments(eventID);
            for (User participant : participants){
                fetchedEmails.add(participant.getEmail());
            }
            String[] emails = fetchedEmails.toArray(new String[0]);
            fetchEventPreparedStatement.close();
            return new Event(eventID, eventName, eventDate, eventTime, duration, location, participants, emails, attachments, reminder, priority);

        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //#####################################################################################//
    //                                                                                     //
    //                                  SUBROUTINES                                        //
    //                                                                                     //
    //#####################################################################################//

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
     * Inserts a new file into the database.
     * Return ID on successful insertion
     *
     * @param eventID the ID of the event to which the file belongs
     * @param attachments the files which should be inserted into the database
     * @throws SQLException if something went wrong with the preparedStatement or resultSet
     * @throws IOException if something went wrong with the file handling
     */
    private static void insertNewAttachments(final int eventID, ArrayList<File> attachments) throws SQLException, IOException {
        FileInputStream fileInputStream = null;

        PreparedStatement insertAttachmentPreparedStatement = connection.prepareStatement(INSERT_NEW_ATTACHMENT_QUERY);
        for (File file : attachments) {
            fileInputStream = new FileInputStream(file);

            insertAttachmentPreparedStatement.setString(1, file.getName());
            insertAttachmentPreparedStatement.setBinaryStream(2, fileInputStream);
            insertAttachmentPreparedStatement.setInt(3, eventID);
            insertAttachmentPreparedStatement.executeUpdate();
        }

        // closing fileInputStream
        if (fileInputStream != null) {
            fileInputStream.close();
        }

        // closing preparedStatement
        insertAttachmentPreparedStatement.close();
    }

    /**
     * Inserts all participants which are in the given event.
     *
     * @param eventID the ID of the event with the participants which should be inserted
     * @param participants list of all participants of this event
     * @throws SQLException when something went wrong with the preparedStatement
     */
    public static void insertNewParticipants(final int eventID, ArrayList<User> participants) throws SQLException {
        PreparedStatement insertNewParticipantPreparedStatement = connection.prepareStatement(INSERT_NEW_PARTICIPANTS_QUERY);

        for (User participant : participants) {
            insertNewParticipantPreparedStatement.setString(1, participant.getUsername());
            insertNewParticipantPreparedStatement.setString(2, participant.getEmail());
            insertNewParticipantPreparedStatement.setInt(3, participant.getId());
            insertNewParticipantPreparedStatement.setInt(4, eventID);
            insertNewParticipantPreparedStatement.executeUpdate();
        }

        insertNewParticipantPreparedStatement.close();
    }

    /**
     * This method should be called everytime a user creates an event for making a
     *      reference in the database, so that one knows which user is going to go to which event.
     *
     * @param userID ID of the user which creates an event
     * @param eventID ID of the event to which the user os going to go
     * @throws SQLException if something went wrong with the preparedStatement
     */
    private static void createUser_EventBridge(final int userID, final int eventID) throws SQLException {
        PreparedStatement userEventBridgePreparedStatement;

        userEventBridgePreparedStatement = connection.prepareStatement(MAKE_USER_EVENT_TABLE_QUERY);
        userEventBridgePreparedStatement.setInt(1, eventID);
        userEventBridgePreparedStatement.setInt(2, userID);
        userEventBridgePreparedStatement.executeUpdate();
    }

    /**
     * Updated the location in the database, if the location changes
     *
     * @param location the location which should be updated in the database
     * @throws SQLException if something went wrong with the preparedStatement
     */
    public static void editLocation(Location location) throws SQLException {
        PreparedStatement editLocationPreparedStatement;

        editLocationPreparedStatement = connection.prepareStatement(EDIT_LOCATION_QUERY);
        prepareLocationInsertion(location, editLocationPreparedStatement);
        editLocationPreparedStatement.setInt(8, location.getLocationID());
        editLocationPreparedStatement.executeUpdate();
        editLocationPreparedStatement.close();
    }

    /**
     * Deletes a file from the database when an event is which includes an attachment
     *      or when the attachment is deleted by user
     *
     * @param eventID ID of the event
     * @throws SQLException if something went wrong with the preparedStatement
     */
    private static void deleteAttachments(final int eventID) throws SQLException {
        PreparedStatement deleteAttachmentPreparedStatement;

        deleteAttachmentPreparedStatement = connection.prepareStatement(DELETE_ATTACHMENTS_QUERY);
        deleteAttachmentPreparedStatement.setInt(1, eventID);
        deleteAttachmentPreparedStatement.executeUpdate();
        deleteAttachmentPreparedStatement.close();
    }

    /**
     * Deletes the location corresponding to the event which is deleted.
     *
     * @param locationID ID of the location
     * @throws SQLException when something went wrong with the preparedStatement
     */
    private static void deleteLocation(final int locationID) throws SQLException {
        PreparedStatement deleteLocationPreparedStatement;

        deleteLocationPreparedStatement = connection.prepareStatement(DELETE_LOCATION_QUERY);
        deleteLocationPreparedStatement.setInt(1, locationID);
        deleteLocationPreparedStatement.executeUpdate();
        deleteLocationPreparedStatement.close();
    }

    /**
     * Deletes the participants corresponding to the event which is deleted.
     *
     * @param eventID the ID of the event which is deleted.
     * @throws SQLException if something went wrong with the preparedStatement.
     */
    private static void deleteParticipants(final int eventID) throws SQLException {
        PreparedStatement deleteParticipantsPreparedStatement;

        deleteParticipantsPreparedStatement = connection.prepareStatement(DELETE_PARTICIPANTS_QUERY);
        deleteParticipantsPreparedStatement.setInt(1, eventID);
        deleteParticipantsPreparedStatement.executeUpdate();
        deleteParticipantsPreparedStatement.close();
    }

    /**
     * Deletes all references from an event for a user from the database.
     *
     * @param userID ID of the user
     * @param eventID ID of the event
     * @throws SQLException if something went wrong with the PreparedStatement
     */
    private static void deleteUser_EventBridge(final int userID, final int eventID) throws SQLException {
        PreparedStatement deleteUserEventBridgePreparedStatement;

        deleteUserEventBridgePreparedStatement = connection.prepareStatement(DELETE_USER_EVENT_BRIDGE_QUERY);
        deleteUserEventBridgePreparedStatement.setInt(1, userID);
        deleteUserEventBridgePreparedStatement.setInt(2, eventID);
        deleteUserEventBridgePreparedStatement.executeUpdate();
        deleteUserEventBridgePreparedStatement.close();
    }

    /**
     * This method just prevents code redundancy in insertNewLocation and editLocation since this code snippet
     *      was in both functions
     *
     * @param location the location which should be edited or inserted
     * @param preparedStatement the preparedStatement
     * @throws SQLException if something went wrong with the preparedStatement
     */
    private static void prepareLocationInsertion(Location location, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, location.getStreet());
        preparedStatement.setString(2, location.getStreetNumber());
        preparedStatement.setString(3, location.getZip());
        preparedStatement.setString(4, location.getCity());
        preparedStatement.setString(5, location.getCountry());
        preparedStatement.setString(6, location.getBuilding());
        preparedStatement.setString(7, location.getRoom());
    }

    /**
     * Makes an entry for a location in the database.
     * Return the ID on successful insertion
     *
     * @param location the location which should be saved in the database
     * @return -1 on unsuccessful insertion
     * @throws SQLException if something went wrong with the preparedStatement or resultSet
     */
    private static int insertNewLocation(Location location) throws SQLException {
        PreparedStatement insertLocationPreparedStatement;
        ResultSet insertLocationResultSet;
        int key;

        insertLocationPreparedStatement = connection.prepareStatement(INSERT_NEW_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS);
        prepareLocationInsertion(location, insertLocationPreparedStatement);
        insertLocationPreparedStatement.executeUpdate();

        // get the ID of the location from the database
        insertLocationResultSet = insertLocationPreparedStatement.getGeneratedKeys();
        if (insertLocationResultSet.next()) {
            key = insertLocationResultSet.getInt(1);
        } else {
            throw new SQLException("could not get the key");
        }

        return key;
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
    private static boolean verify(ResultSet resultSet, final String searchWith, final String credential, final String password) throws SQLException {
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
     * This method fetches the location where the event is going to take place.
     *
     * @param locationID ID of the location (this is the foreign key if the entity "event")
     * @return object with the location details
     * @throws SQLException if something went wrong with the preparedStatement
     */
    private static Location fetchLocationFromEvent(final int locationID) throws SQLException {
        PreparedStatement fetchLocationPreparedStatement;
        ResultSet fetchLocationResultSet;
        fetchLocationPreparedStatement = connection.prepareStatement(GET_LOCATION_FROM_EVENT_QUERY);
        fetchLocationPreparedStatement.setInt(1, locationID);
        fetchLocationResultSet = fetchLocationPreparedStatement.executeQuery();
        if (fetchLocationResultSet.next()) {
            int locationId = fetchLocationResultSet.getInt(1);
            String street = fetchLocationResultSet.getString(2);
            String houseNumber = fetchLocationResultSet.getString(3);
            String zip = fetchLocationResultSet.getString(4);
            String city = fetchLocationResultSet.getString(5);
            String country = fetchLocationResultSet.getString(6);
            String building = fetchLocationResultSet.getString(7);
            String room = fetchLocationResultSet.getString(8);

            return new Location(locationId, street, houseNumber, zip, city, country, building, room);
        }
        fetchLocationPreparedStatement.close();

        return null;
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
    private static User getUser(ResultSet resultSet, final String searchWith, final String credential) throws SQLException {
        User user = null;

        while (resultSet.next()) {
            if (resultSet.getString(searchWith).equals(credential)) {
                int userID = resultSet.getInt("userID");
                String firstname = resultSet.getString("firstName");
                String lastname = resultSet.getString("lastName");
                String username = resultSet.getString("userName");
                String email = resultSet.getString("email");
                user = new User(userID, firstname, lastname, username, email);

                break;
            }
        }

        return user;
    }

    /**
     * This method fetches all the participants of an event.
     *
     * @param eventID ID of the event (primary key of the entity "Event")
     * @return participants - returns a list of participants which participate in the event with the
     *      given eventID
     * @throws SQLException if something went wrong if the preparedStatement or resultSet
     */
    private static ArrayList<User> fetchParticipants(final int eventID) throws SQLException {
        PreparedStatement fetchParticipantsPreparedStatement;
        ResultSet fetchParticipantsResultSet;
        ArrayList<User> participants = new ArrayList<>();

        fetchParticipantsPreparedStatement = connection.prepareStatement(GET_PARTICIPANTS_FROM_EVENT_QUERY);
        fetchParticipantsPreparedStatement.setInt(1, eventID);
        fetchParticipantsResultSet = fetchParticipantsPreparedStatement.executeQuery();
        while (fetchParticipantsResultSet.next()) {
            String username = fetchParticipantsResultSet.getString("username");
            String email = fetchParticipantsResultSet.getString("email");
            int userID = fetchParticipantsResultSet.getInt("userID");

            participants.add(new User(username, email, userID));
        }
        fetchParticipantsPreparedStatement.close();

        return participants;
    }

    /**
     * This method fetches all attachments for an event.
     *
     * @param eventID ID of the event
     * @return returns a list with all the files which are attached to the given event
     * @throws SQLException if something went wrong with the preparedStatement or resultSet
     * @throws IOException if something went wrong with the file handling
     */
    private static ArrayList<File> fetchAttachments(final int eventID) throws SQLException, IOException {
        PreparedStatement fetchAttachmentPreparedStatement;
        ResultSet fetchAttachmentResultSet;
        ArrayList<File> attachments = new ArrayList<>();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        File file;


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
        fetchAttachmentPreparedStatement.close();

        // closing input and output streams
        if (outputStream != null) outputStream.close();
        if (inputStream != null) inputStream.close();

        return attachments;
    }
}