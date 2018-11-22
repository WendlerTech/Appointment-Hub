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

    //Appointment table
    private static final String TBL_APPT = "appointment";
    private static final String APPT_COL0 = "appointmentId";
    private static final String APPT_COL1 = "customerId";
    private static final String APPT_COL2 = "userId";
    private static final String APPT_COL3 = "title";
    private static final String APPT_COL4 = "description";
    private static final String APPT_COL5 = "location";
    private static final String APPT_COL6 = "contact";
    private static final String APPT_COL7 = "type";
    private static final String APPT_COL8 = "url";
    private static final String APPT_COL9 = "start";
    private static final String APPT_COL10 = "end";
    private static final String APPT_COL11 = "createDate";
    private static final String APPT_COL12 = "createdBy";
    private static final String APPT_COL13 = "lastUpdate";
    private static final String APPT_COL14 = "lastUpdateBy";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public static User addNewUser(String username, String password) throws SQLException {
        User newUser = null;
        String queryString = "INSERT INTO " + TBL_USER
                + " (" + USER_COL1 + ", " + USER_COL2 + ", " + USER_COL3 + ", " + USER_COL4
                + ", " + USER_COL5 + ", " + USER_COL6 + ", " + USER_COL7 + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString,
                        Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "1");
            statement.setString(4, username);
            statement.setString(5, getCurrentDate());
            statement.setString(6, getCurrentDate());
            statement.setString(7, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                newUser = new User();
                newUser.setUserName(username);
                newUser.setPassword(password);
                try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                    newUser.setUserID(generatedKey.getInt(1));
                }
                return newUser;
            }
        }
        return newUser;
    }

    public static User userLogin(String username, String password) {
        User loggedInUser = null;
        //Verifies login data. Cast as binary makes password case-sensative.
        String queryString = "SELECT * FROM user WHERE userName = ? AND "
                + "CAST(password AS BINARY) = ?;";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                loggedInUser = new User();
                loggedInUser.setUserName(username);
                loggedInUser.setPassword(password);
                loggedInUser.setUserID(resultSet.getInt("userId"));

                return loggedInUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loggedInUser;
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

    public static boolean addNewAppointment(Appointment appt) throws SQLException {
        String queryString = "INSERT INTO " + TBL_APPT + " (" + APPT_COL1
                + ", " + APPT_COL2 + ", " + APPT_COL3 + ", " + APPT_COL4
                + ", " + APPT_COL5 + ", " + APPT_COL6 + ", " + APPT_COL7
                + ", " + APPT_COL8 + ", " + APPT_COL9 + ", " + APPT_COL10
                + ", " + APPT_COL11 + ", " + APPT_COL12 + ", " + APPT_COL13
                + ", " + APPT_COL14 + ") VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(
                        queryString);) {
            statement.setInt(1, appt.getCustomerID());
            statement.setInt(2, appt.getUserID());
            statement.setString(3, appt.getTitle());
            statement.setString(4, appt.getDescription());
            statement.setString(5, appt.getLocation());
            statement.setString(6, appt.getContact());
            statement.setString(7, appt.getType());
            statement.setString(8, appt.getUrl());
            statement.setString(9, appt.getStartTime());
            statement.setString(10, appt.getEndTime());
            statement.setString(11, appt.getCreateDate());
            statement.setString(12, appt.getCreatedBy());
            statement.setString(13, appt.getLastUpdate());
            statement.setString(14, appt.getLastUpdateBy());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

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

    public static ObservableList getAppointmentList() throws SQLException {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        String queryString = "SELECT * FROM " + TBL_APPT;
        ResultSet result;
        Appointment appt;

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            result = statement.executeQuery();

            while (result.next()) {
                appt = new Appointment();
                appt.setAppointmentID(result.getInt(APPT_COL0));
                appt.setCustomerID(result.getInt(APPT_COL1));
                appt.setUserID(result.getInt(APPT_COL2));
                appt.setTitle(result.getString(APPT_COL3));
                appt.setDescription(result.getString(APPT_COL4));
                appt.setLocation(result.getString(APPT_COL5));
                appt.setContact(result.getString(APPT_COL6));
                appt.setType(result.getString(APPT_COL7));
                appt.setUrl(result.getString(APPT_COL8));
                appt.setStartTime(result.getString(APPT_COL9));
                appt.setEndTime(result.getString(APPT_COL10));
                appt.setCreateDate(result.getString(APPT_COL11));
                appt.setCreatedBy(result.getString(APPT_COL12));
                appt.setLastUpdate(result.getString(APPT_COL13));
                appt.setLastUpdateBy(result.getString(APPT_COL14));

                apptList.add(appt);
            }
        }

        return apptList;
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
