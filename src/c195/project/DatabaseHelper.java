package c195.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nick Wendler
 */
public class DatabaseHelper {

    //Connection properties
    private static final String DATABASE_NAME = "U04S7q";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/"
            + DATABASE_NAME;
    private static final String USERNAME = "U04S7q";
    private static final String PASSWORD = "53688328683";

    //User table
    private static final String TBL_USER = "user";
    private static final String COL0 = "userId";
    private static final String COL1 = "userName";
    private static final String COL2 = "password";
    private static final String COL3 = "active";
    private static final String COL4 = "createBy";
    private static final String COL5 = "createDate";
    private static final String COL6 = "lastUpdate";
    private static final String COL7 = "lastUpdatedBy";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public static void addNewUser(String username, String password) throws SQLException {
        String queryString = "INSERT INTO " + TBL_USER
                + " (" + COL1 + ", " + COL2 + ", " + COL3 + ", " + COL4
                + ", " + COL5 + ", " + COL6 + ", " + COL7 + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "1");
            statement.setString(4, username);
            statement.setString(5, getCurrentDate());
            statement.setString(6, getCurrentDate());
            statement.setString(7, username);

            statement.execute();
            System.out.println("User added successfully");
        }
    }
    
    //Returns true if username has already been registered
    public static boolean checkIfUserExists(String username) throws SQLException {
        String queryString = "SELECT * FROM " + TBL_USER + " WHERE username = ?;";
        ResultSet result;
        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setString(1, username);
            result = statement.executeQuery();
            
            return result.next();
        }
    }

    public static String getCurrentDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }
}
