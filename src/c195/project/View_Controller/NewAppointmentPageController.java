package c195.project.View_Controller;

import c195.project.Address;
import c195.project.Appointment;
import c195.project.C195ProjectWendler;
import c195.project.Customer;
import c195.project.DatabaseHelper;
import c195.project.SearchCombo;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
public class NewAppointmentPageController implements Initializable {

    @FXML
    private TextField txtNewApptTitle;
    @FXML
    private ComboBox<String> cmbNewApptType;
    @FXML
    private ComboBox<Customer> cmbNewApptCust;
    @FXML
    private TextField txtNewApptContact;
    @FXML
    private DatePicker dateNewAppt;
    @FXML
    private ChoiceBox<Integer> cmbNewApptHrStart;
    @FXML
    private ChoiceBox<String> cmbNewApptMinStart;
    @FXML
    private ChoiceBox<Integer> cmbNewApptHrEnd;
    @FXML
    private ChoiceBox<String> cmbNewApptMinEnd;
    @FXML
    private ChoiceBox<String> cmbNewApptLocation;
    @FXML
    private TextArea txtNewApptDescription;
    @FXML
    private Button btnSaveNewAppt;
    @FXML
    private Button btnClearNewAppt;
    @FXML
    private Button btnCancelNewAppt;
    @FXML
    private Button btnNewApptViewCust;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private Appointment appointment;
    private Customer customer = null;
    private Address address = null;
    private ObservableList<Customer> custList = null;
    private ObservableList<Address> addrList = null;
    private boolean firstLoad = true;
    private int selectedStartHour;
    final private ObservableList hourList = FXCollections.observableArrayList(
            8, 9, 10, 11, 12, 1, 2, 3, 4);
    private ObservableList endHourList = FXCollections.observableArrayList(
            8, 9, 10, 11, 12, 1, 2, 3, 4);
    final private ObservableList minuteList = FXCollections.observableArrayList(
            "00", "15", "30", "45");
    private ObservableList endMinuteList = FXCollections.observableArrayList(
            "00", "15", "30", "45");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList locationList = FXCollections.observableArrayList(
                "London, England", "Phoenix, Arizona", "New York, New York");
        ObservableList apptTypeList = FXCollections.observableArrayList(
                "Consultation", "Accounting", "Sales", "Support");
        try {
            custList = DatabaseHelper.getCustomerList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cmbNewApptCust.setItems(custList);
        cmbNewApptHrStart.setItems(hourList);
        cmbNewApptHrEnd.setItems(endHourList);
        cmbNewApptMinStart.setItems(minuteList);
        cmbNewApptMinEnd.setItems(minuteList);
        cmbNewApptLocation.setItems(locationList);
        cmbNewApptType.setItems(apptTypeList);

        cmbNewApptCust.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object == null ? "" : object.getCustName();
            }

            @Override
            public Customer fromString(String string) {
                return cmbNewApptCust.getValue();
            }
        });
        cmbNewApptCust.setEditable(true);
        SearchCombo.autoCompleteComboBoxPlus(cmbNewApptCust, (typedText, Customer)
                -> Customer.getCustName().toLowerCase().contains(typedText.toLowerCase()));

        cmbNewApptHrEnd.setDisable(true);
        cmbNewApptMinEnd.setDisable(true);
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    @FXML
    void cancelNewApptButtonHandler(ActionEvent event) {
        //Closes window
        AppointmentsPageController.setDataWasUpdated(false);
        currentStage.close();
    }

    @FXML
    void clearNewApptButtonHandler(ActionEvent event) {
        //Clears all fields
        txtNewApptTitle.clear();
        cmbNewApptType.getSelectionModel().clearSelection();
        cmbNewApptCust.getSelectionModel().clearSelection();
        txtNewApptContact.clear();
        dateNewAppt.getEditor().clear(); //Clears textfield
        dateNewAppt.setValue(null); //Clears value
        cmbNewApptHrStart.getSelectionModel().clearSelection();
        cmbNewApptMinStart.getSelectionModel().clearSelection();
        cmbNewApptHrEnd.getSelectionModel().clearSelection();
        cmbNewApptMinEnd.getSelectionModel().clearSelection();
        cmbNewApptLocation.getSelectionModel().clearSelection();
        txtNewApptDescription.clear();

        //Resets start/end time combo boxes
        endHourList = FXCollections.observableArrayList(
                8, 9, 10, 11, 12, 1, 2, 3, 4);
        endMinuteList = FXCollections.observableArrayList(
                "00", "15", "30", "45");
        cmbNewApptHrEnd.setItems(endHourList);
        cmbNewApptMinEnd.setItems(endMinuteList);
        cmbNewApptHrEnd.setDisable(true);
        cmbNewApptMinEnd.setDisable(true);

        //Resets selected customer
        customer = null;
        address = null;
    }

    @FXML
    void viewCustNewApptButtonHandler(ActionEvent event) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void cmbNewApptStartTimeHandler(ActionEvent event) {
        selectedStartHour = cmbNewApptHrStart.getSelectionModel().getSelectedIndex();
        if (selectedStartHour == 8) {
            //Automatically sets end time to 5:00PM if after 4PM is chosen
            cmbNewApptHrEnd.setDisable(false);
            cmbNewApptMinEnd.setDisable(false);
            endHourList = FXCollections.observableArrayList(
                    5);
            endMinuteList = FXCollections.observableArrayList(
                    "00");
            cmbNewApptHrEnd.setItems(endHourList);
            cmbNewApptMinEnd.setItems(endMinuteList);
            cmbNewApptHrEnd.getSelectionModel().selectFirst();
            cmbNewApptMinEnd.getSelectionModel().selectFirst();
        } else if (selectedStartHour >= 0) {
            //Disallows setting end time before start time
            endHourList = FXCollections.observableArrayList(
                    8, 9, 10, 11, 12, 1, 2, 3, 4);
            endMinuteList = FXCollections.observableArrayList(
                    "00", "15", "30", "45");
            cmbNewApptHrEnd.setDisable(false);
            cmbNewApptMinEnd.setDisable(false);
            endHourList.remove(0, selectedStartHour + 1);
            cmbNewApptHrEnd.setItems(endHourList);
            cmbNewApptMinEnd.setItems(endMinuteList);
        }
    }

    @FXML
    void cmbNewApptCustHandler(ActionEvent event) {
        if (SearchCombo.getComboBoxValue(cmbNewApptCust) != null) {
            if (firstLoad) {
                firstLoad = false;
                try {
                    addrList = DatabaseHelper.getAddressList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (Address addr : addrList) {
                if (addr.getAddressID() == SearchCombo.getComboBoxValue(cmbNewApptCust).getAddressID()) {
                    //Address record matches customer's
                    txtNewApptContact.setText(addr.getPhoneNumber());
                    customer = SearchCombo.getComboBoxValue(cmbNewApptCust);
                    address = addr;
                    break; //Break loop once match is found
                }
            }
        }
    }

    @FXML
    void saveNewAppointmentButtonHandler(ActionEvent event) throws SQLException {

        if (customer != null && cmbNewApptHrStart.getValue() != null
                && cmbNewApptMinStart.getValue() != null
                && cmbNewApptHrEnd.getValue() != null
                && cmbNewApptMinEnd.getValue() != null
                && dateNewAppt.getValue() != null
                && cmbNewApptType.getValue() != null
                && cmbNewApptLocation.getValue() != null
                && txtNewApptTitle.getText().length() > 0
                && txtNewApptContact.getText().length() > 0
                && txtNewApptDescription.getText().length() > 0) {
            appointment = new Appointment();
            appointment.setCustomerID(customer.getCustID());
            appointment.setUserID(mainApp.getCurrentUser().getUserID());
            appointment.setTitle(txtNewApptTitle.getText());
            appointment.setDescription(txtNewApptDescription.getText());
            appointment.setLocation(cmbNewApptLocation.getSelectionModel().getSelectedItem());
            appointment.setContact(txtNewApptContact.getText());
            appointment.setType(cmbNewApptType.getSelectionModel().getSelectedItem());
            appointment.setUrl(mainApp.getUrl());
            appointment.setStartTime(getApptStartTime());
            appointment.setEndTime(getApptEndTime());
            appointment.setCreateDate(DatabaseHelper.getCurrentDate());
            appointment.setCreatedBy(mainApp.getCurrentUserName());
            appointment.setLastUpdate(DatabaseHelper.getCurrentDate());
            appointment.setLastUpdateBy(mainApp.getCurrentUserName());

            if (DatabaseHelper.addNewAppointment(appointment)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Appointment saved successfully.");
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.showAndWait();
                AppointmentsPageController.setDataWasUpdated(true);
                currentStage.close();
            } else {
                showErrorAlert("Error while saving appointment.");
            }
        } else {
            showErrorAlert("Please fill in all appropriate fields.");
        }
    }

    private String getApptStartTime() {
        int hrStart = cmbNewApptHrStart.getValue();
        String minStart = cmbNewApptMinStart.getValue();
        LocalDate date = dateNewAppt.getValue();

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
        int hrEnd = cmbNewApptHrEnd.getValue();
        String minEnd = cmbNewApptMinEnd.getValue();
        LocalDate date = dateNewAppt.getValue();

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

    //Shows an alert pop-up with no icon or header text
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }
}
