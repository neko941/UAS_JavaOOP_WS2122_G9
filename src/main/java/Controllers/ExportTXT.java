package Controllers;

import ExternalConnections.DBConn;
import Models.Event;
import Models.User;

import java.sql.*;
import java.sql.Date;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static Controllers.Debugging.printNotificationInConsole;
import static ExternalConnections.DBUtilities.fetchAllEventsFromUser;

// import java.time.LocalDate;

/**
 * @author tysunqua
 *
 * A Java program that exports data from any table to Txt file.
 */
public class ExportTXT {
    /**
     * A Function to choose a name of the table to export
     *
     * @param
     */
    public void export(User user) throws IOException {
        try {
            ArrayList<Event> eventList = fetchAllEventsFromUser(user);
            String time = String.format("%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            FileWriter txtFileName = new FileWriter(String.format("%s _ Events of %s.txt", time, user.getUsername()));

            txtFileName.write("eventID; eventName; eventDate; eventTime; duration; location");

            for (Event event : eventList) {
                txtFileName.write(String.format("\n%s", event.toString()));
            }
            txtFileName.close();
            printNotificationInConsole("TXT file exported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
