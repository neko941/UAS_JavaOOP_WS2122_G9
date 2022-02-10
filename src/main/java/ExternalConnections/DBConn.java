package ExternalConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.Debugging.printNotificationInConsole;

/**
 * @author tysunqua
 *
 * A Java class to establish the connection to the databases
 */

public class DBConn {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            setConnection();
        }

        return connection;
    }

    /**
     *
     *
     * A method to set a connection to our database
     */

    public static void setConnection() {
        String jdbcURL = getDataFromConfig("database", "url");
        String username = getDataFromConfig("database", "username");
        String password = getDataFromConfig("database", "password");

        // need jav driver mysql connector
        // https://dev.mysql.com/downloads/connector/j/
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection == null) {
                printNotificationInConsole("Cannot connect database");
            } else {
                printNotificationInConsole("Connect database successfully");
            }
        }
    }
}
