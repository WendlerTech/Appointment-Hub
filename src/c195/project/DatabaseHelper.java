package c195.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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
    static Connection connection;
    
    //Table columns
    
    
    
    public static void openConnection() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Database connection opened successfully.");
    }
    
    public static void closeConnection() throws SQLException {
        connection.close();
        System.out.println("Database connection closed successfully.");
    }
}
