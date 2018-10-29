package c195.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private static final String USER_COL0 = "userId";
    private static final String USER_COL1 = "userName";
    private static final String USER_COL2 = "password";
    private static final String USER_COL3 = "active";
    private static final String USER_COL4 = "createBy";
    private static final String USER_COL5 = "createDate";
    private static final String USER_COL6 = "lastUpdate";
    private static final String USER_COL7 = "lastUpdatedBy";

    //Address table
    private static final String TBL_ADDRESS = "address";
    private static final String ADDR_COL0 = "addressId";
    private static final String ADDR_COL1 = "address";
    private static final String ADDR_COL2 = "address2";
    private static final String ADDR_COL3 = "cityId";
    private static final String ADDR_COL4 = "postalCode";
    private static final String ADDR_COL5 = "phone";
    private static final String ADDR_COL6 = "createDate";
    private static final String ADDR_COL7 = "createdBy";
    private static final String ADDR_COL8 = "lastUpdate";
    private static final String ADDR_COL9 = "lastUpdateBy";

    //Customer table
    private static final String TBL_CUST = "customer";
    private static final String CUST_COL0 = "customerId";
    private static final String CUST_COL1 = "customerName";
    private static final String CUST_COL2 = "addressId";
    private static final String CUST_COL3 = "active";
    private static final String CUST_COL4 = "createDate";
    private static final String CUST_COL5 = "createdBy";
    private static final String CUST_COL6 = "lastUpdate";
    private static final String CUST_COL7 = "lastUpdateBy";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public static void addNewUser(String username, String password) throws SQLException {
        String queryString = "INSERT INTO " + TBL_USER
                + " (" + USER_COL1 + ", " + USER_COL2 + ", " + USER_COL3 + ", " + USER_COL4
                + ", " + USER_COL5 + ", " + USER_COL6 + ", " + USER_COL7 + ") "
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
        }
    }

    public static void addNewCustomer(Customer cust, Address address) throws SQLException {
        String queryString = "INSERT INTO " + TBL_ADDRESS + " (" + ADDR_COL1
                + ", " + ADDR_COL2 + ", " + ADDR_COL3 + ", " + ADDR_COL4
                + ", " + ADDR_COL5 + ", " + ADDR_COL6 + ", " + ADDR_COL7
                + ", " + ADDR_COL8 + ", " + ADDR_COL9 + ") VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection();) {
            try (PreparedStatement statement = conn.prepareStatement(queryString,
                    Statement.RETURN_GENERATED_KEYS);) {
                statement.setString(1, address.getAddress());
                statement.setString(2, address.getAddress2());
                statement.setString(3, String.valueOf(address.getCityID()));
                statement.setString(4, address.getZipCode());
                statement.setString(5, address.getPhoneNumber());
                statement.setString(6, address.getCreateDate());
                statement.setString(7, address.getCreatedBy());
                statement.setString(8, address.getLastUpdated());
                statement.setString(9, address.getLastUpdatedBy());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No rows affected, "
                            + "address insertion failed.");
                }

                try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                    if (generatedKey.next()) {
                        cust.setAddressID(generatedKey.getInt(1));
                    } else {
                        throw new SQLException("No ID obtained, "
                                + "address ID update failed.");
                    }
                }
            }

            queryString = "INSERT INTO " + TBL_CUST + " (" + CUST_COL1
                    + ", " + CUST_COL2 + ", " + CUST_COL3 + ", " + CUST_COL4
                    + ", " + CUST_COL5 + ", " + CUST_COL6 + ", " + CUST_COL7
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement statement = conn.prepareStatement(queryString,
                    Statement.RETURN_GENERATED_KEYS);) {
                statement.setString(1, cust.getCustName());
                statement.setString(2, String.valueOf(cust.getAddressID()));
                statement.setString(3, String.valueOf(cust.isIsActive()));
                statement.setString(4, cust.getCreateDate());
                statement.setString(5, cust.getCreatedBy());
                statement.setString(6, cust.getLastUpdate());
                statement.setString(7, cust.getLastUpdatedBy());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No rows affected, "
                            + "customer insertion failed.");
                }
            }
        }
    }

    public static ObservableList getCustomerList() throws SQLException {
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        String queryString = "SELECT * FROM " + TBL_CUST;
        ResultSet result;
        Customer cust;

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            result = statement.executeQuery();
            
            while (result.next()) {
                cust = new Customer();
                cust.setCustID(result.getInt(CUST_COL0));
                cust.setCustName(result.getString(CUST_COL1));
                cust.setAddressID(result.getInt(CUST_COL2));
                cust.setIsActive(result.getBoolean(CUST_COL3));
                cust.setCreateDate(result.getString(CUST_COL4));
                cust.setCreatedBy(result.getString(CUST_COL5));
                cust.setLastUpdate(result.getString(CUST_COL6));
                cust.setLastUpdatedBy(result.getString(CUST_COL7));
                
                custList.add(cust);
            }
        }
        return custList;
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

    public static int getCityID(String cityName) {
        switch (cityName) {
            case "Phoenix":
                return 1;
            case "New York":
                return 2;
            case "Seattle":
                return 3;
            case "Houston":
                return 4;
            case "Denver":
                return 5;
            case "London":
                return 6;
            case "Manchester":
                return 7;
            case "Liverpool":
                return 8;
            case "Birmingham":
                return 9;
            default:
                return 0;
        }
    }

    public static String getCurrentDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }
}
