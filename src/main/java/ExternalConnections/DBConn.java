package ExternalConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Controllers.ConfigController.getDataFromConfig;

public class DBConn {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            setConnection();
        }

        return connection;
    }

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
                System.out.println("Cannot connect database");
            } else {
                System.out.println("Connect database successfully");
            }
        }
    }
}
