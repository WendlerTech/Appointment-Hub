package c195.project.View_Controller;

import c195.project.Address;
import c195.project.Appointment;
import c195.project.C195ProjectWendler;
import c195.project.Customer;
import c195.project.DatabaseHelper;
import c195.project.SearchCombo;
import c195.project.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class ReportsPageController implements Initializable {

    @FXML
    private ComboBox<String> cmbAvailableReports;
    @FXML
    private ComboBox<String> cmbReportsApptType;
    @FXML
    private ComboBox<String> cmbReportsMonth;
    @FXML
    private ComboBox<User> cmbReportsConsultantName;
    @FXML
    private ComboBox<String> cmbReportsCountry;
    @FXML
    private ComboBox<String> cmbReportsCity;
    @FXML
    private TableView<?> tblReports;
    private TableView<Appointment> tblApptByType;
    private TableView<Appointment> tblConsultants;
    private TableView<Customer> tblCustByCity;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private ObservableList<Appointment> apptList, apptListToDisplay, scheduleListToDisplay;
    private ObservableList<Customer> custList, custListToDisplay;
    private ObservableList<Address> addrList;
    private ObservableList<User> userList;
    private final ObservableList cityListEngland = FXCollections.observableArrayList(
            "London", "Manchester", "Liverpool", "Birmingham");
    private final ObservableList cityListUSA = FXCollections.observableArrayList(
            "Phoenix", "New York", "Seattle", "Houston", "Denver");

    private final DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
    private final ZoneId zoneId = ZoneId.systemDefault();
    private ZonedDateTime zdt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> monthList = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November",
                "December");
        ObservableList apptTypeList = FXCollections.observableArrayList(
                "Consultation", "Accounting", "Sales", "Support");
        ObservableList countryList = FXCollections.observableArrayList(
                "England", "USA");

        cmbAvailableReports.getItems().add("Customers By City");
        cmbAvailableReports.getItems().add("Consultant Schedule");
        cmbAvailableReports.getItems().add("Appointment Types By Month");

        cmbReportsMonth.setDisable(true);
        cmbReportsMonth.setPromptText("Select a type");
        cmbReportsCity.setDisable(true);
        cmbReportsCity.setPromptText("Choose a country");
        cmbReportsCountry.setItems(countryList);
        cmbReportsMonth.setItems(monthList);
        cmbReportsApptType.setItems(apptTypeList);

        cmbReportsConsultantName.setEditable(true);
    }

    private void getData() {
        try {
            if (mainApp.isInitialDataChanged()) {
                apptList = DatabaseHelper.getAppointmentList();
            } else {
                apptList = mainApp.getAppointmentList();
            }
            userList = DatabaseHelper.getUserList();
            custList = DatabaseHelper.getCustomerList();
            addrList = DatabaseHelper.getAddressList();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;

        getData();

        cmbReportsConsultantName.setItems(userList);

        cmbReportsConsultantName.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User object) {
                return object == null ? "" : object.getUserName();
            }

            @Override
            public User fromString(String string) {
                return cmbReportsConsultantName.getValue();
            }
        });

        SearchCombo.autoCompleteComboBoxPlus(cmbReportsConsultantName, (typedText, User)
                -> User.getUserName().toLowerCase().contains(typedText.toLowerCase()));
    }

    @FXML
    void custGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }

    @FXML
    void reportsComboBoxEventHandler(ActionEvent event) {
        //Shows & hides appropriate combo boxes depending on user selection
        switch (cmbAvailableReports.getValue()) {
            case "Customers By City":
                cmbReportsCity.setVisible(true);
                cmbReportsCountry.setVisible(true);
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsConsultantName.setVisible(false);
                break;
            case "Consultant Schedule":
                cmbReportsConsultantName.setVisible(true);
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
                break;
            case "Appointment Types By Month":
                cmbReportsApptType.setVisible(true);
                cmbReportsMonth.setVisible(true);
                cmbReportsConsultantName.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
                break;
            default:
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsConsultantName.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
        }
    }

    @FXML
    void countryComboEventHandler(ActionEvent event) {
        //Displays cities based on which country the user has selected
        if (cmbReportsCountry.getValue() != null) {
            switch (cmbReportsCountry.getValue()) {
                case "USA":
                    cmbReportsCity.setItems(cityListUSA);
                    cmbReportsCity.setDisable(false);
                    cmbReportsCity.setPromptText("Select a city");
                    break;
                case "England":
                    cmbReportsCity.setItems(cityListEngland);
                    cmbReportsCity.setDisable(false);
                    cmbReportsCity.setPromptText("Select a city");
                    break;
                default:
                    cmbReportsCity.setDisable(true);
                    cmbReportsCity.setPromptText("Choose a country");
                    break;
            }
        }
    }

    @FXML
    void apptTypeComboEventHandler(ActionEvent event) {
        //Displays cities based on which country the user has selected
        if (cmbReportsApptType.getValue() != null) {
            cmbReportsMonth.setDisable(false);
            cmbReportsMonth.getSelectionModel().clearSelection();
            cmbReportsMonth.setPromptText("Select a month");
        } else {
            cmbReportsMonth.getSelectionModel().clearSelection();
            cmbReportsMonth.setDisable(true);
            cmbReportsMonth.setPromptText("Select a type");
        }
    }

    private LocalDate getSelectedMonth() {
        LocalDate selectedMonth = LocalDate.now();
        return selectedMonth.withMonth(cmbReportsMonth.getSelectionModel().getSelectedIndex() + 1);
    }

    @FXML
    void populateCustomersByCity(ActionEvent event) throws SQLException {
        if (cmbReportsCity.getSelectionModel().getSelectedItem() != null) {
            custListToDisplay = FXCollections.observableArrayList();
            custListToDisplay.clear();

            //Creates new tableview object
            tblCustByCity = new TableView<>();
            tblCustByCity.getColumns().clear();

            //Creates new table columns
            TableColumn<Customer, String> colName = new TableColumn<>("Name");
            colName.setCellValueFactory(cellData -> new SimpleStringProperty(capitalizeFirstLetter(cellData.getValue().getCustName())));
            tblCustByCity.getColumns().add(colName);

            TableColumn<Customer, String> colCustSince = new TableColumn<>("Customer Since");
            colCustSince.setCellValueFactory(cellData -> new SimpleStringProperty(formatCustSinceDate(cellData.getValue().getCreateDate())));
            tblCustByCity.getColumns().add(colCustSince);

            TableColumn<Customer, String> colCreatedBy = new TableColumn<>("Created By");
            colCreatedBy.setCellValueFactory(cellData -> new SimpleStringProperty(capitalizeFirstLetter(cellData.getValue().getCreatedBy())));
            tblCustByCity.getColumns().add(colCreatedBy);

            int selectedCityId = DatabaseHelper.getCityID(cmbReportsCity.getSelectionModel().getSelectedItem());

            custList.forEach((cust) -> {
                for (Address addr : addrList) {
                    if (cust.getAddressID() == addr.getAddressID()) {
                        if (addr.getCityID() == selectedCityId) {
                            custListToDisplay.add(cust);
                            break;
                        }
                    }
                }
            });

            TableColumn<Customer, Boolean> colActive = new TableColumn<>("Active");
            colActive.setCellValueFactory(cellData -> getIsActive(cellData.getValue().isIsActive()));
            colActive.setCellFactory(CheckBoxTableCell.forTableColumn(colActive));
            tblCustByCity.getColumns().add(colActive);

            tblCustByCity.setItems(custListToDisplay);
            tblCustByCity.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tblReports.getColumns().clear();
            tblReports.setPlaceholder(tblCustByCity);
        }
    }

    @FXML
    void populateConsultantSchedules(ActionEvent event) throws SQLException {
        if (cmbReportsConsultantName.getSelectionModel().getSelectedItem() != null) {
            scheduleListToDisplay = FXCollections.observableArrayList();
            scheduleListToDisplay.clear();

            //Creates new tableview object
            tblConsultants = new TableView<>();
            tblConsultants.getColumns().clear();

            //Creates new table columns
            TableColumn<Appointment, String> colTitle = new TableColumn<>("Title");
            colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
            tblConsultants.getColumns().add(colTitle);

            TableColumn<Appointment, String> colType = new TableColumn<>("Type");
            colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
            tblConsultants.getColumns().add(colType);

            TableColumn<Appointment, String> colLocation = new TableColumn<>("Location");
            colLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
            tblConsultants.getColumns().add(colLocation);

            TableColumn<Appointment, String> colTime = new TableColumn<>("Time");
            colTime.setCellValueFactory(cellData -> new SimpleStringProperty(formatColDate(cellData.getValue().getStartTime())));
            tblConsultants.getColumns().add(colTime);

            for (Appointment appt : apptList) {
                if (appt.getUserID() == cmbReportsConsultantName.getSelectionModel().getSelectedItem().getUserID()) {
                    scheduleListToDisplay.add(appt);
                }
            }
            tblConsultants.setItems(scheduleListToDisplay);
            tblConsultants.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tblReports.getColumns().clear();
            tblReports.setPlaceholder(tblConsultants);
        }
    }

    @FXML
    void populateApptTypeByMonth(ActionEvent event) throws SQLException {
        if (cmbReportsMonth.getSelectionModel().getSelectedItem() != null) {
            apptListToDisplay = FXCollections.observableArrayList();
            apptListToDisplay.clear();
            LocalDate apptDate;
            Date date = new Date();

            //Creates new tableview object
            tblApptByType = new TableView<>();
            tblApptByType.getColumns().clear();

            //Creates new table columns
            TableColumn<Appointment, String> colTitle = new TableColumn<>("Title");
            colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
            tblApptByType.getColumns().add(colTitle);

            TableColumn<Appointment, String> colContact = new TableColumn<>("Contact");
            colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
            tblApptByType.getColumns().add(colContact);

            TableColumn<Appointment, String> colLocation = new TableColumn<>("Location");
            colLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
            tblApptByType.getColumns().add(colLocation);

            TableColumn<Appointment, String> colConsultant = new TableColumn<>("Consultant");
            colConsultant.setCellValueFactory(cellData -> new SimpleStringProperty(capitalizeFirstLetter(cellData.getValue().getCreatedBy())));
            tblApptByType.getColumns().add(colConsultant);

            //Searches for appointment types within the selected month
            for (Appointment appt : apptList) {
                if (appt.getType().equals(cmbReportsApptType.getSelectionModel().getSelectedItem())) {
                    try {
                        //Converts string into date object
                        date = formatDay.parse(appt.getStartTime());
                    } catch (ParseException ex) {
                        Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Converts date object into LocalDate
                    Instant instant = date.toInstant();
                    zdt = ZonedDateTime.ofInstant(instant, zoneId);
                    apptDate = LocalDate.from(zdt);

                    if (apptDate.getMonth().equals(getSelectedMonth().getMonth())) {
                        apptListToDisplay.add(appt);
                    }
                }
            }
            tblApptByType.setItems(apptListToDisplay);
            tblApptByType.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tblReports.getColumns().clear();
            tblReports.setPlaceholder(tblApptByType);
        }
    }

    private SimpleBooleanProperty getIsActive(int isActive) {
        SimpleBooleanProperty isCustActive = new SimpleBooleanProperty();
        if (isActive > 0) {
            isCustActive.setValue(true);
        } else {
            isCustActive.setValue(false);
        }
        return isCustActive;
    }

    private String formatColDate(String dateToFormat) {
        //Takes in string date
        DateFormat formatFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("EEE, MMM d, h:mm a");
        Date date = new Date();
        try {
            //Converts string to date object
            date = formatFullDate.parse(dateToFormat);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Formats date object, returns as string
        return format.format(date);
    }

    private String formatCustSinceDate(String dateToFormat) {
        //Takes in string date
        DateFormat formatFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("MMM d, yyyy");
        Date date = new Date();
        try {
            //Converts string to date object
            date = formatFullDate.parse(dateToFormat);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Formats date object, returns as string
        return format.format(date);
    }

    public String capitalizeFirstLetter(String original) {
        //Capitalizes first letter of a string       
        if (original == null || original.length() == 0) {
            return original;
        } else {
            original = original.toLowerCase();
            return original.substring(0, 1).toUpperCase() + original.substring(1);
        }
    }
}
