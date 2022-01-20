package ExternalConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            setConnection();
        }

        return connection;
    }

    public static void setConnection() {
        String jdbcURL = "jdbc:mysql://bkyhmn5ukri7jfw1kpjs-mysql.services.clever-cloud.com:3306/bkyhmn5ukri7jfw1kpjs";
        String username = "upqkamkqerixvpbb";
        String password = "dNJnm1pH1qC7uSU2IgrJ";

        // need jav driver mysql connector
        // https://dev.mysql.com/downloads/connector/j/
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        if (connection == null) {
            System.out.println("Connection failed");
        } else {
            System.out.println("Connection successful");
        }

    }
}
