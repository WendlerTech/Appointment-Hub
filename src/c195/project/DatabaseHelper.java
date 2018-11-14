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
            //Adds address, returns the generated PK
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

                //If address was added, uses generated PK as address ID
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

    public static void updateCustomer(Customer cust, Address address) throws SQLException {
        String queryString = "UPDATE " + TBL_CUST + " SET " + CUST_COL1
                + " = ?, " + CUST_COL2 + " = ?, " + CUST_COL3 + " = ?, "
                + CUST_COL4 + " = ?, " + CUST_COL5 + " = ?, "
                + CUST_COL6 + " = ?, " + CUST_COL7 + " = ? "
                + "WHERE " + CUST_COL0 + " = ?;";

        try (Connection conn = getConnection();) {
            try (PreparedStatement statement = conn.prepareStatement(
                    queryString);) {
                statement.setString(1, cust.getCustName());
                statement.setInt(2, cust.getAddressID());
                statement.setInt(3, cust.isIsActive());
                statement.setString(4, cust.getCreateDate());
                statement.setString(5, cust.getCreatedBy());
                statement.setString(6, cust.getLastUpdate());
                statement.setString(7, cust.getLastUpdatedBy());
                statement.setInt(8, cust.getCustID());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No rows affected, "
                            + "customer insertion failed.");
                }
            }

            queryString = "UPDATE " + TBL_ADDRESS + " SET " + ADDR_COL1
                    + " = ?, " + ADDR_COL2 + " = ?, " + ADDR_COL3
                    + " = ?, " + ADDR_COL4 + " = ?, " + ADDR_COL5
                    + " = ?, " + ADDR_COL6 + " = ?, " + ADDR_COL7
                    + " = ?, " + ADDR_COL8 + " = ?, " + ADDR_COL9
                    + " = ? WHERE " + ADDR_COL0 + " = ?;";

            try (PreparedStatement statement = conn.prepareStatement(
                    queryString);) {
                statement.setString(1, address.getAddress());
                statement.setString(2, address.getAddress2());
                statement.setInt(3, address.getCityID());
                statement.setString(4, address.getZipCode());
                statement.setString(5, address.getPhoneNumber());
                statement.setString(6, address.getCreateDate());
                statement.setString(7, address.getCreatedBy());
                statement.setString(8, address.getLastUpdated());
                statement.setString(9, address.getLastUpdatedBy());
                statement.setInt(10, address.getAddressID());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No rows affected, "
                            + "customer insertion failed.");
                }
            }
        }
    }

    public static boolean deleteCustomer(int custID, int addressID) throws SQLException {
        String queryString = "DELETE FROM " + TBL_CUST + " WHERE " + CUST_COL0
                + " = ?;";

        try (Connection conn = getConnection();) {
            try (PreparedStatement statement = conn.prepareStatement(
                    queryString);) {
                statement.setInt(1, custID);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    //Customer deleting has failed
                    return false;
                }
            }

            queryString = "DELETE FROM " + TBL_ADDRESS + " WHERE " + ADDR_COL0
                    + " = ?;";

            try (PreparedStatement statement = conn.prepareStatement(
                    queryString);) {
                statement.setInt(1, addressID);

                int affectedRows = statement.executeUpdate();

                //Returns false if address deleting fails, otherwise true
                return affectedRows != 0;
            }
        }
    }

    public static Customer getCustomer(int custID) throws SQLException {
        Customer cust = null;
        ResultSet result;
        String queryString = "SELECT * FROM " + TBL_CUST + " WHERE "
                + CUST_COL0 + " = ?;";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setInt(1, custID);
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
            }
        }
        return cust;
    }

    public static Address getAddress(int addressID) throws SQLException {
        Address addr = null;
        ResultSet result;
        String queryString = "SELECT * FROM " + TBL_ADDRESS + " WHERE "
                + ADDR_COL0 + " = ?;";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setInt(1, addressID);
            result = statement.executeQuery();

            while (result.next()) {
                addr = new Address();
                addr.setAddressID(result.getInt(ADDR_COL0));
                addr.setAddress(result.getString(ADDR_COL1));
                addr.setAddress2(result.getString(ADDR_COL2));
                addr.setCityID(result.getInt(ADDR_COL3));
                addr.setZipCode(result.getString(ADDR_COL4));
                addr.setPhoneNumber(result.getString(ADDR_COL5));
                addr.setCreateDate(result.getString(ADDR_COL6));
                addr.setCreatedBy(result.getString(ADDR_COL7));
                addr.setLastUpdated(result.getString(ADDR_COL8));
                addr.setLastUpdatedBy(result.getString(ADDR_COL9));
            }
        }
        return addr;
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

    public static ObservableList getAddressList() throws SQLException {
        ObservableList<Address> addressList = FXCollections.observableArrayList();
        String queryString = "SELECT * FROM " + TBL_ADDRESS;
        ResultSet result;
        Address addr;

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            result = statement.executeQuery();

            while (result.next()) {
                addr = new Address();
                addr.setAddressID(result.getInt(ADDR_COL0));
                addr.setAddress(result.getString(ADDR_COL1));
                addr.setAddress2(result.getString(ADDR_COL2));
                addr.setCityID(result.getInt(ADDR_COL3));
                addr.setZipCode(result.getString(ADDR_COL4));
                addr.setPhoneNumber(result.getString(ADDR_COL5));
                addr.setCreateDate(result.getString(ADDR_COL6));
                addr.setCreatedBy(result.getString(ADDR_COL7));
                addr.setLastUpdated(result.getString(ADDR_COL8));
                addr.setLastUpdatedBy(result.getString(ADDR_COL9));

                addressList.add(addr);
            }
        }
        return addressList;
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

    public static String getCityName(int cityId) {
        switch (cityId) {
            case 1:
                return "Phoenix";
            case 2:
                return "New York";
            case 3:
                return "Seattle";
            case 4:
                return "Houston";
            case 5:
                return "Denver";
            case 6:
                return "London";
            case 7:
                return "Manchester";
            case 8:
                return "Liverpool";
            case 9:
                return "Birmingham";
            default:
                return "";
        }
    }

    public static String getCurrentDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }
}
