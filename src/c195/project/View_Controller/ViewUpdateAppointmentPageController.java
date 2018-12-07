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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class ViewUpdateAppointmentPageController implements Initializable {

    @FXML
    private TextField txtViewApptTitle;
    @FXML
    private ComboBox<String> cmbViewApptType;
    @FXML
    private ComboBox<Customer> cmbViewApptCust;
    @FXML
    private TextField txtViewApptContact;
    @FXML
    private DatePicker dateViewAppt;
    @FXML
    private ChoiceBox<String> cmbViewApptHrStart;
    @FXML
    private ChoiceBox<String> cmbViewApptMinStart;
    @FXML
    private ChoiceBox<String> cmbViewApptHrEnd;
    @FXML
    private ChoiceBox<String> cmbViewApptMinEnd;
    @FXML
    private ChoiceBox<String> cmbViewApptLocation;
    @FXML
    private TextArea txtViewApptDescription;
    @FXML
    private Label lblViewApptConsultant;
    @FXML
    private Label lblUpdateStartTime;
    @FXML
    private Label lblUpdateEndTime;
    @FXML
    private Label lblUpdateTime2;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private Appointment appointment;
    private User currentUser;
    private Customer customer = null;
    private Address address = null;
    private ObservableList<Customer> custList = null;
    private ObservableList<Address> addrList = null;
    private int selectedStartHour;
    private static boolean openedFromCalendar = false;

    final private ObservableList locationList = FXCollections.observableArrayList(
            "London, England", "Phoenix, Arizona", "New York, New York");
    final private ObservableList apptTypeList = FXCollections.observableArrayList(
            "Consultation", "Accounting", "Sales", "Support");
    final private ObservableList hourList = FXCollections.observableArrayList(
            "8", "9", "10", "11", "12", "1", "2", "3", "4");
    private ObservableList endHourList = FXCollections.observableArrayList(
            "8", "9", "10", "11", "12", "1", "2", "3", "4");
    final private ObservableList minuteList = FXCollections.observableArrayList(
            "00", "15", "30", "45");
    private ObservableList endMinuteList = FXCollections.observableArrayList(
            "00", "15", "30", "45");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbViewApptHrStart.setItems(hourList);
        cmbViewApptHrEnd.setItems(endHourList);
        cmbViewApptMinStart.setItems(minuteList);
        cmbViewApptMinEnd.setItems(endMinuteList);
        cmbViewApptLocation.setItems(locationList);
        cmbViewApptType.setItems(apptTypeList);

        try {
            custList = DatabaseHelper.getCustomerList();
            cmbViewApptCust.setItems(custList);
        } catch (SQLException ex) {
            Logger.getLogger(ViewUpdateAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbViewApptCust.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object == null ? "" : object.getCustName();
            }

            @Override
            public Customer fromString(String string) {
                return cmbViewApptCust.getValue();
            }
        });

        cmbViewApptCust.setEditable(true);
        SearchCombo.autoCompleteComboBoxPlus(cmbViewApptCust, (typedText, Customer)
                -> Customer.getCustName().toLowerCase().contains(typedText.toLowerCase()));
    }

    public void setStage(Stage stage, Appointment apptToUpdate, User currentUser) {
        currentStage = stage;
        appointment = apptToUpdate;
        this.currentUser = currentUser;
        try {
            populateData();
        } catch (SQLException ex) {
            Logger.getLogger(ViewUpdateAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void populateData() throws SQLException {
        for (Customer cust : custList) {
            if (cust.getCustID() == appointment.getCustomerID()) {
                customer = cust;
                break;
            }
        }
        lblViewApptConsultant.setText("Consultant: " + capitalizeFirstLetter(appointment.getCreatedBy()));
        txtViewApptTitle.setText(appointment.getTitle());
        cmbViewApptType.getSelectionModel().select(appointment.getType());
        cmbViewApptCust.getSelectionModel().select(customer);
        txtViewApptContact.setText(appointment.getContact());
        dateViewAppt.setValue(getDatePickerFormat(appointment.getStartTime()));
        cmbViewApptHrStart.getSelectionModel().select(getHourFromDate(appointment.getStartTime()));
        cmbViewApptHrEnd.getSelectionModel().select(getHourFromDate(appointment.getEndTime()));
        cmbViewApptMinStart.getSelectionModel().select(getMinFromDate(appointment.getStartTime()));
        cmbViewApptMinEnd.getSelectionModel().select(getMinFromDate(appointment.getEndTime()));
        cmbViewApptLocation.getSelectionModel().select(appointment.getLocation());
        txtViewApptDescription.setText(appointment.getDescription());
        createObjectsOnLoad();
        updateTimezoneLabels();
    }

    @FXML
    void saveUpdatedAppointmentButtonHandler(ActionEvent event) throws SQLException {
        if (customer != null && cmbViewApptHrStart.getValue() != null
                && cmbViewApptMinStart.getValue() != null
                && cmbViewApptHrEnd.getValue() != null
                && cmbViewApptMinEnd.getValue() != null
                && dateViewAppt.getValue() != null
                && cmbViewApptType.getValue() != null
                && cmbViewApptLocation.getValue() != null
                && txtViewApptTitle.getText().length() > 0
                && txtViewApptContact.getText().length() > 0
                && txtViewApptDescription.getText().length() > 0) {
            appointment.setCustomerID(customer.getCustID());
            appointment.setTitle(txtViewApptTitle.getText());
            appointment.setDescription(txtViewApptDescription.getText());
            appointment.setLocation(cmbViewApptLocation.getSelectionModel().getSelectedItem());
            appointment.setContact(txtViewApptContact.getText());
            appointment.setType(cmbViewApptType.getSelectionModel().getSelectedItem());
            appointment.setUrl(mainApp.getUrl());
            appointment.setStartTime(getApptStartTime());
            appointment.setEndTime(getApptEndTime());
            appointment.setLastUpdate(DatabaseHelper.getCurrentDate());
            appointment.setLastUpdateBy(mainApp.getCurrentUserName());

            if (!checkForScheduleConflicts()) {
                if (DatabaseHelper.updateAppointment(appointment)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Appointment updated successfully.");
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    AppointmentsPageController.setDataWasUpdated(true);
                    CalendarPageController.setDataWasUpdated(true);
                    mainApp.setInitialDataChanged(true);
                    currentStage.close();
                } else {
                    showErrorAlert("Error while updating appointment.");
                }
            }
        } else {
            showErrorAlert("Please fill in all appropriate fields.");
        }
    }

    void createObjectsOnLoad() {
        //Allows the "View Customer" button to work before selecting a customer
        try {
            addrList = DatabaseHelper.getAddressList();
        } catch (SQLException ex) {
            Logger.getLogger(ViewUpdateAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Address addr : addrList) {
            if (addr.getAddressID() == customer.getAddressID()) {
                //Address record matches customer's
                address = addr;
                break; //Break loop once match is found
            }
        }
    }

    @FXML
    void cmbViewApptCustHandler(ActionEvent event) {
        //Autofills contact data based on selected customer
        if (SearchCombo.getComboBoxValue(cmbViewApptCust) != null) {
            for (Address addr : addrList) {
                if (addr.getAddressID() == SearchCombo.getComboBoxValue(cmbViewApptCust).getAddressID()) {
                    //Address record matches customer's
                    txtViewApptContact.setText(addr.getPhoneNumber());
                    customer = SearchCombo.getComboBoxValue(cmbViewApptCust);
                    address = addr;
                    break; //Break loop once match is found
                }
            }
        }
    }

    @FXML
    void cmbViewApptStartTimeHandler(ActionEvent event) {
        selectedStartHour = cmbViewApptHrStart.getSelectionModel().getSelectedIndex();
        if (selectedStartHour == 8) {
            //Automatically sets end time to 5:00PM if after 4PM is chosen
            cmbViewApptHrEnd.setDisable(false);
            cmbViewApptMinEnd.setDisable(false);
            endHourList = FXCollections.observableArrayList(
                    "5");
            endMinuteList = FXCollections.observableArrayList(
                    "00");
            cmbViewApptHrEnd.setItems(endHourList);
            cmbViewApptMinEnd.setItems(endMinuteList);
            cmbViewApptHrEnd.getSelectionModel().selectFirst();
            cmbViewApptMinEnd.getSelectionModel().selectFirst();
        } else if (selectedStartHour >= 0) {
            //Disallows setting end time before start time
            endHourList = FXCollections.observableArrayList(
                    "8", "9", "10", "11", "12", "1", "2", "3", "4");
            endMinuteList = FXCollections.observableArrayList(
                    "00", "15", "30", "45");
            cmbViewApptHrEnd.setDisable(false);
            cmbViewApptMinEnd.setDisable(false);
            endHourList.remove(0, selectedStartHour + 1);
            cmbViewApptHrEnd.setItems(endHourList);
            cmbViewApptMinEnd.setItems(endMinuteList);
        }
        updateTimezoneLabels();
    }

    @FXML
    void cmbViewApptTimeRefreshHandler(ActionEvent event) {
        updateTimezoneLabels();
    }

    @FXML
    void viewCustViewAppointmentButtonHandler(ActionEvent event) {
        //Opens the update customer page if a customer is selected
        if (customer != null && address != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/UpdateCustomerPage.fxml"));
                Parent root = loader.load();
                UpdateCustomerPageController controller = loader.getController();

                Stage stage = new Stage();
                controller.setStage(stage, customer, address,
                        mainApp.getCurrentUserName());
                stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));

                stage.setScene(new Scene(root));
                stage.setTitle("View or update an existing customer");
                stage.showAndWait();
                refreshData();
            } catch (IOException ex) {
                Logger.getLogger(ViewUpdateAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void clearViewApptButtonHandler(ActionEvent event) {
        //Clears all fields
        txtViewApptTitle.clear();
        cmbViewApptType.getSelectionModel().clearSelection();
        cmbViewApptCust.getSelectionModel().clearSelection();
        txtViewApptContact.clear();
        dateViewAppt.getEditor().clear(); //Clears textfield
        dateViewAppt.setValue(null); //Clears value
        cmbViewApptHrStart.getSelectionModel().clearSelection();
        cmbViewApptMinStart.getSelectionModel().clearSelection();
        cmbViewApptHrEnd.getSelectionModel().clearSelection();
        cmbViewApptMinEnd.getSelectionModel().clearSelection();
        cmbViewApptLocation.getSelectionModel().clearSelection();
        txtViewApptDescription.clear();

        //Resets start/end time combo boxes
        endHourList = FXCollections.observableArrayList(
                "8", "9", "10", "11", "12", "1", "2", "3", "4");
        endMinuteList = FXCollections.observableArrayList(
                "00", "15", "30", "45");
        cmbViewApptHrEnd.setItems(endHourList);
        cmbViewApptMinEnd.setItems(endMinuteList);
        cmbViewApptHrEnd.setDisable(true);
        cmbViewApptMinEnd.setDisable(true);

        //Resets selected customer
        customer = null;
        address = null;
    }

    private void refreshData() {
        //If user updates customer name via the View button, change is applied
        if (CustomerPageController.isDataWasUpdated()) {
            try {
                cmbViewApptCust.getSelectionModel().clearSelection();
                populateData();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public LocalDate getDatePickerFormat(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(date, format);
        return localDate;
    }

    public String getHourFromDate(String date) {
        //Grabs hour from date, parses into int
        int startHour = Integer.parseInt(date.substring(11, 13));
        if (startHour > 12) {
            //Handles military time for display purposes
            startHour -= 12;
            //Converts back to string so combo can select object, not index
            return "" + startHour;
        } else {
            return "" + startHour;
        }
    }

    public String getMinFromDate(String date) {
        return date.substring(14, 16);
    }

    @FXML
    void cancelViewApptButtonHandler(ActionEvent event) {
        //Closes window
        AppointmentsPageController.setDataWasUpdated(false);
        currentStage.close();
    }

    private void updateTimezoneLabels() {
        if (cmbViewApptHrStart.getValue() != null
                && cmbViewApptMinStart.getValue() != null
                && cmbViewApptHrEnd.getValue() != null
                && cmbViewApptMinEnd.getValue() != null
                && dateViewAppt.getValue() != null
                && cmbViewApptLocation.getValue() != null) {
            TimeZone originalTZ = TimeZone.getDefault();
            lblUpdateTime2.setVisible(true);
            lblUpdateStartTime.setVisible(true);
            lblUpdateEndTime.setVisible(true);

            switch (cmbViewApptLocation.getSelectionModel().getSelectedItem()) {
                case "London, England":
                    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
                    break;
                case "Phoenix, Arizona":
                    TimeZone.setDefault(TimeZone.getTimeZone("US/Arizona"));
                    break;
                case "New York, New York":
                    TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
                    break;
                default:
                    TimeZone.setDefault(originalTZ);
            }

            Calendar startTimeCal = Calendar.getInstance();
            Calendar endTimeCal = Calendar.getInstance();
            LocalDate date = dateViewAppt.getValue();

            //============Start Time============
            int hrStart = Integer.parseInt(cmbViewApptHrStart.getValue());
            int minStart = Integer.parseInt(cmbViewApptMinStart.getValue());
            int startMilitaryDifferential;
            int startHour;
            String startMinuteLeadDigit;
            String startAMPM;

            if (hrStart < 7) {
                hrStart += 12; //Allows for 24h format
            }

            //Sets calendar object to the user's input
            startTimeCal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), hrStart, minStart);

            //Formats minutes for label
            if (minStart > 0) {
                startMinuteLeadDigit = ":" + startTimeCal.get(Calendar.MINUTE);
            } else {
                startMinuteLeadDigit = ":0" + startTimeCal.get(Calendar.MINUTE);
            }

            //============End Time============
            int hrEnd = Integer.parseInt(cmbViewApptHrEnd.getValue());
            int minEnd = Integer.parseInt(cmbViewApptMinEnd.getValue());
            int endMilitaryDifferential;
            int endHour;
            String endMinuteLeadDigit;
            String endAMPM;

            if (hrEnd < 7) {
                hrEnd += 12; //Allows for 24h format
            }

            endTimeCal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), hrEnd, minEnd);

            if (minEnd > 0) {
                endMinuteLeadDigit = ":" + endTimeCal.get(Calendar.MINUTE);
            } else {
                endMinuteLeadDigit = ":0" + endTimeCal.get(Calendar.MINUTE);
            }

            //After adding times to the calendar object, applies current user's timezone
            TimeZone.setDefault(originalTZ);
            startTimeCal.setTimeZone(originalTZ);
            endTimeCal.setTimeZone(originalTZ);

            //Decides whether to display AM or PM
            if (startTimeCal.get(Calendar.HOUR_OF_DAY) > 12) {
                startMilitaryDifferential = 12;
                startAMPM = "PM";
            } else if (startTimeCal.get(Calendar.HOUR_OF_DAY) == 12) {
                startMilitaryDifferential = 0;
                startAMPM = "PM";
            } else {
                startMilitaryDifferential = 0;
                startAMPM = "AM";
            }

            if (endTimeCal.get(Calendar.HOUR_OF_DAY) > 12) {
                endMilitaryDifferential = 12;
                endAMPM = "PM";
            } else if (endTimeCal.get(Calendar.HOUR_OF_DAY) == 12) {
                endMilitaryDifferential = 0;
                endAMPM = "PM";
            } else {
                endMilitaryDifferential = 0;
                endAMPM = "AM";
            }

            //Formats from military time
            startHour = startTimeCal.get(Calendar.HOUR_OF_DAY) - startMilitaryDifferential;
            endHour = endTimeCal.get(Calendar.HOUR_OF_DAY) - endMilitaryDifferential;

            //Updates labels
            lblUpdateStartTime.setText("" + startHour + startMinuteLeadDigit + " " + startAMPM + " - ");
            lblUpdateEndTime.setText("" + endHour + endMinuteLeadDigit + " " + endAMPM);
        } else {
            lblUpdateTime2.setVisible(false);
            lblUpdateStartTime.setVisible(false);
            lblUpdateEndTime.setVisible(false);
        }
    }

    private String getApptStartTime() {
        int hrStart = Integer.parseInt(cmbViewApptHrStart.getValue());
        String minStart = cmbViewApptMinStart.getValue();
        LocalDate date = dateViewAppt.getValue();

        if (hrStart < 7) {
            hrStart += 12;
            return getFormattedDate(date) + " " + hrStart + ":" + minStart + ":00";
        } else if (hrStart <= 9) {
            return getFormattedDate(date) + " 0" + hrStart + ":" + minStart + ":00";
        } else {
            return getFormattedDate(date) + " " + hrStart + ":" + minStart + ":00";
        }
    }

    private String getApptEndTime() {
        int hrEnd = Integer.parseInt(cmbViewApptHrEnd.getValue());
        String minEnd = cmbViewApptMinEnd.getValue();
        LocalDate date = dateViewAppt.getValue();

        if (hrEnd < 7) {
            hrEnd += 12;
            return getFormattedDate(date) + " " + hrEnd + ":" + minEnd + ":00";
        } else if (hrEnd <= 9) {
            return getFormattedDate(date) + " 0" + hrEnd + ":" + minEnd + ":00";
        } else {
            return getFormattedDate(date) + " " + hrEnd + ":" + minEnd + ":00";
        }
    }

    private String getFormattedDate(LocalDate date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateToFormat = Date.from(instant);

        return format.format(dateToFormat);
    }

    private boolean checkForScheduleConflicts() {
        ObservableList<Appointment> apptList;
        if (!openedFromCalendar) {
            apptList = AppointmentsPageController.getApptList();
        } else {
            apptList = CalendarPageController.getApptList();
        }
        LocalDate selectedDay = dateViewAppt.getValue();
        LocalDate startDayToCompare;
        LocalDateTime startTimeToCompare, endTimeToCompare,
                selectedStartTime, selectedEndTime;
        DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt;
        for (Appointment appt : apptList) {
            //Ignores currently selected appointment
            if (appt.getAppointmentID() != appointment.getAppointmentID()) {
                //Only checks against appointments created by same consultant
                if (appt.getUserID() == mainApp.getCurrentUser().getUserID()) {
                    //Date object is created using appointment start time
                    try {
                        date = formatDay.parse(appt.getStartTime());
                    } catch (ParseException ex) {
                        Logger.getLogger(NewAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Parses date into LocalDate
                    Instant instant = date.toInstant();
                    zdt = ZonedDateTime.ofInstant(instant, zoneId);
                    startDayToCompare = LocalDate.from(zdt);

                    //If the new appointment falls on the same day as an existing one
                    if (startDayToCompare.isEqual(selectedDay)) {
                        //-----Handles startTimeToCompare-----
                        try {
                            date = formatFullDate.parse(appt.getStartTime());
                        } catch (ParseException ex) {
                            Logger.getLogger(NewAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        instant = date.toInstant();
                        zdt = ZonedDateTime.ofInstant(instant, zoneId);
                        startTimeToCompare = LocalDateTime.from(zdt);

                        //-----Handles endTimeToCompare-----
                        try {
                            date = formatFullDate.parse(appt.getEndTime());
                        } catch (ParseException ex) {
                            Logger.getLogger(NewAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        instant = date.toInstant();
                        zdt = ZonedDateTime.ofInstant(instant, zoneId);
                        endTimeToCompare = LocalDateTime.from(zdt);

                        //-----Handles selectedStartTime-----
                        try {
                            date = formatFullDate.parse(getApptStartTime());
                        } catch (ParseException ex) {
                            Logger.getLogger(NewAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        instant = date.toInstant();
                        zdt = ZonedDateTime.ofInstant(instant, zoneId);
                        selectedStartTime = LocalDateTime.from(zdt);

                        //-----Handles selectedEndTime-----
                        try {
                            date = formatFullDate.parse(getApptEndTime());
                        } catch (ParseException ex) {
                            Logger.getLogger(NewAppointmentPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        instant = date.toInstant();
                        zdt = ZonedDateTime.ofInstant(instant, zoneId);
                        selectedEndTime = LocalDateTime.from(zdt);

                        if (selectedStartTime.isEqual(startTimeToCompare) || selectedEndTime.isEqual(endTimeToCompare)
                                || selectedStartTime.isAfter(startTimeToCompare) && selectedStartTime.isBefore(endTimeToCompare)
                                || selectedEndTime.isAfter(startTimeToCompare) && selectedEndTime.isBefore(endTimeToCompare)
                                || selectedStartTime.isBefore(startTimeToCompare) && selectedEndTime.isAfter(endTimeToCompare)) {
                            //Conflicting appointment times found
                            showErrorAlert(capitalizeFirstLetter(mainApp.getCurrentUserName()) + ", you already have an appointment titled \""
                                    + appt.getTitle() + "\" scheduled within your selected time."
                                    + "\nPlease select another time or delete the other appointment.");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }

    public static boolean isOpenedFromCalendar() {
        return openedFromCalendar;
    }

    public static void setOpenedFromCalendar(boolean openedFromCalendar) {
        ViewUpdateAppointmentPageController.openedFromCalendar = openedFromCalendar;
    }

    //Shows an alert pop-up with no icon or header text
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
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
