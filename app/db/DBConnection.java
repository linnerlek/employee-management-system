package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("dbconfig.properties");
            props.load(fis);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Failed to load dbconfig.properties");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
