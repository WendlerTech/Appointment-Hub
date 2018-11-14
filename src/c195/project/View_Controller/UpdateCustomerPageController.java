package c195.project.View_Controller;

import c195.project.Address;
import c195.project.C195ProjectWendler;
import c195.project.Customer;
import c195.project.DatabaseHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class UpdateCustomerPageController implements Initializable {

    @FXML
    private TextField txtUpdateCustName;
    @FXML
    private TextField txtUpdateCustPhone;
    @FXML
    private ComboBox<String> cmbUpdateCustCountry;
    @FXML
    private ComboBox<String> cmbUpdateCustCity;
    @FXML
    private TextField txtUpdateCustAddress1;
    @FXML
    private TextField txtUpdateCustAddress2;
    @FXML
    private TextField txtUpdateCustZip;
    @FXML
    private CheckBox chkUpdateCustActive;
    @FXML
    private Button btnUpdateCustomer;
    @FXML
    private Button btnUpdateCustClear;
    @FXML
    private Button btnUpdateCustCancel;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private Customer customer;
    private Address address;
    private int custId, addrId;
    private String currentUser;

    private final ObservableList cityListUSA = FXCollections.observableArrayList(
            "Phoenix", "New York", "Seattle", "Houston", "Denver");
    private final ObservableList cityListEngland = FXCollections.observableArrayList(
            "London", "Manchester", "Liverpool", "Birmingham");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Auto-adds hyphens to phone number field
        txtUpdateCustPhone.setTextFormatter(new TextFormatter<>(change -> {
            final int oldLength = change.getControlText().length();
            int newLength = change.getControlNewText().length();

            //Backspace
            if (newLength < oldLength) {
                return change;
            }

            switch (newLength) {
                //Adds a hyphen after the first 3 numbers (area code)
                case 3:
                    change.setText(change.getText() + "-");
                    newLength++;
                    break;
                //Adds a hyphen after the first 7 numbers (area code + prefix)
                case 7:
                    change.setText(change.getText() + "-");
                    newLength++;
                    break;
                //Doesn't allow more than 12 characters (10 numbers + 2 hyphens)
                case 13:
                    return null;
            }
            change.setCaretPosition(newLength);
            change.setAnchor(newLength);
            return change;
        }));

        ObservableList countryList = FXCollections.observableArrayList(
                "USA", "England");
        cmbUpdateCustCountry.setItems(countryList);
        CustomerPageController.setDataWasUpdated(false);
    }

    public void setStage(Stage stage, Customer custToUpdate,
            Address addrToUpdate, String currentUser) {
        currentStage = stage;
        customer = custToUpdate;
        address = addrToUpdate;
        this.currentUser = currentUser;
        populateData(customer, address);
    }

    public void populateData(Customer cust, Address addr) {
        txtUpdateCustName.setText(cust.getCustName());
        txtUpdateCustPhone.setText(addr.getPhoneNumber());
        txtUpdateCustAddress1.setText(addr.getAddress());
        txtUpdateCustAddress2.setText(addr.getAddress2());
        txtUpdateCustZip.setText(addr.getZipCode());
        chkUpdateCustActive.setSelected(cust.isIsActive() > 0);

        if (addr.getCityID() > 5) {
            cmbUpdateCustCity.setItems(cityListEngland);
            cmbUpdateCustCountry.getSelectionModel().select(1);
            cmbUpdateCustCity.getSelectionModel().select(addr.getCityID() - 6);
        } else {
            cmbUpdateCustCity.setItems(cityListUSA);
            cmbUpdateCustCountry.getSelectionModel().select(0);
            //Array starts at 0, but DB starts at 1
            cmbUpdateCustCity.getSelectionModel().select(addr.getCityID() - 1);
        }
    }

    @FXML
    void updateCustomerButtonHandler(ActionEvent event) {
        String custName = txtUpdateCustName.getText();
        String address1 = txtUpdateCustAddress1.getText();
        String address2 = txtUpdateCustAddress2.getText();
        String zipCode = txtUpdateCustZip.getText();
        String phoneNum = txtUpdateCustPhone.getText();
        boolean isActive = chkUpdateCustActive.isSelected();

        if (custName.length() > 0 && address1.length() > 0
                && zipCode.length() > 0 && phoneNum.length() > 0
                && cmbUpdateCustCity.getValue() != null) {
            int cityID = DatabaseHelper.getCityID(cmbUpdateCustCity.getValue());

            address.setCityID(cityID);
            address.setAddress(address1);
            address.setAddress2(address2);
            address.setZipCode(zipCode);
            address.setPhoneNumber(phoneNum);
            address.setCreatedBy(currentUser);
            address.setLastUpdatedBy(currentUser);
            address.setCreateDate(DatabaseHelper.getCurrentDate());
            address.setLastUpdated(DatabaseHelper.getCurrentDate());
            customer.setCustName(custName);
            customer.setIsActive(isActive);
            customer.setCreateDate(DatabaseHelper.getCurrentDate());
            customer.setLastUpdate(DatabaseHelper.getCurrentDate());
            customer.setCreatedBy(currentUser);
            customer.setLastUpdatedBy(currentUser);

            try {
                DatabaseHelper.updateCustomer(customer, address);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Customer updated successfully.");
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.showAndWait();
                CustomerPageController.setDataWasUpdated(true);
                currentStage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Please fill out all fields.");
        }
    }

    @FXML
    void clearUpdateCustButtonHandler(ActionEvent event) {
        //Clears all fields
        txtUpdateCustName.clear();
        txtUpdateCustPhone.clear();
        txtUpdateCustAddress1.clear();
        txtUpdateCustAddress2.clear();
        txtUpdateCustZip.clear();
        cmbUpdateCustCountry.getSelectionModel().clearSelection();
        cmbUpdateCustCity.getSelectionModel().clearSelection();
        chkUpdateCustActive.setSelected(false);
        cmbUpdateCustCity.setDisable(true);
        cmbUpdateCustCity.setPromptText("Select a country");
    }

    @FXML
    void countryComboEventHandler(ActionEvent event) {
        //Shows & hides appropriate combo boxes depending on user selection
        if (cmbUpdateCustCountry.getValue() != null) {
            switch (cmbUpdateCustCountry.getValue()) {
                case "USA":
                    cmbUpdateCustCity.setItems(cityListUSA);
                    cmbUpdateCustCity.setDisable(false);
                    cmbUpdateCustCity.setPromptText("Select a city");
                    break;
                case "England":
                    cmbUpdateCustCity.setItems(cityListEngland);
                    cmbUpdateCustCity.setDisable(false);
                    cmbUpdateCustCity.setPromptText("Select a city");
                    break;
                default:
                    cmbUpdateCustCity.setDisable(true);
                    cmbUpdateCustCity.setPromptText("Select a country");
                    break;
            }
        }
    }

    @FXML
    void cancelUpdateCustButtonHandler(ActionEvent event) {
        //Closes window
        CustomerPageController.setDataWasUpdated(false);
        currentStage.close();
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }

    public void showErrorAlert(String message) {
        //Shows an alert pop-up with no icon or header text
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
