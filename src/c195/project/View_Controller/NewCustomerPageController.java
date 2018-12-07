package c195.project.View_Controller;

import c195.project.Address;
import c195.project.Customer;
import c195.project.DatabaseHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
public class NewCustomerPageController implements Initializable {

    @FXML
    private TextField txtNewCustName;
    @FXML
    private TextField txtNewCustPhone;
    @FXML
    private ComboBox<String> cmbNewCustCountry;
    @FXML
    private ComboBox<String> cmbNewCustCity;
    @FXML
    private TextField txtNewCustAddress1;
    @FXML
    private TextField txtNewCustAddress2;
    @FXML
    private TextField txtNewCustZip;
    @FXML
    private CheckBox chkNewCustActive;

    private Stage currentStage;
    private Customer newCust;
    private Address custAddress;
    private String currentUser;

    private final ObservableList cityListUSA = FXCollections.observableArrayList(
            "Phoenix", "New York", "Seattle", "Houston", "Denver");
    private final ObservableList cityListEngland = FXCollections.observableArrayList(
            "London", "Manchester", "Liverpool", "Birmingham");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Auto-adds hyphens to phone number field
        txtNewCustPhone.setTextFormatter(new TextFormatter<>(change -> {
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

        cmbNewCustCountry.setItems(countryList);
        CustomerPageController.setDataWasUpdated(false);
    }

    public void setStage(Stage stage, String currentUser) {
        currentStage = stage;
        this.currentUser = currentUser;
    }

    @FXML
    void saveNewCustButtonHandler(ActionEvent event) {
        custAddress = new Address();
        newCust = new Customer();

        String custName = txtNewCustName.getText();
        String address1 = txtNewCustAddress1.getText();
        String address2 = txtNewCustAddress2.getText();
        String zipCode = txtNewCustZip.getText();
        String phoneNum = txtNewCustPhone.getText();
        boolean isActive = chkNewCustActive.isSelected();

        if (custName.length() > 0 && address1.length() > 0
                && zipCode.length() > 0 && phoneNum.length() > 0
                && cmbNewCustCity.getValue() != null) {
            int cityID = DatabaseHelper.getCityID(cmbNewCustCity.getValue());

            custAddress.setCityID(cityID);
            custAddress.setAddress(address1);
            custAddress.setAddress2(address2);
            custAddress.setZipCode(zipCode);
            custAddress.setPhoneNumber(phoneNum);
            custAddress.setCreatedBy(currentUser);
            custAddress.setLastUpdatedBy(currentUser);
            custAddress.setCreateDate(DatabaseHelper.getCurrentDate());
            custAddress.setLastUpdated(DatabaseHelper.getCurrentDate());
            newCust.setCustName(custName);
            newCust.setIsActive(isActive);
            newCust.setCreateDate(DatabaseHelper.getCurrentDate());
            newCust.setLastUpdate(DatabaseHelper.getCurrentDate());
            newCust.setCreatedBy(currentUser);
            newCust.setLastUpdatedBy(currentUser);

            try {
                DatabaseHelper.addNewCustomer(newCust, custAddress);
            } catch (SQLException ex) {
                Logger.getLogger(NewCustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Customer successfully added.");
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.showAndWait();
            CustomerPageController.setDataWasUpdated(true);
            currentStage.close();
        } else {
            showErrorAlert("Please fill out all fields.");
        }
    }

    @FXML
    void cancelNewCustButtonHandler(ActionEvent event) {
        //Closes window
        CustomerPageController.setDataWasUpdated(false);
        currentStage.close();
    }

    @FXML
    void clearNewCustButtonHandler(ActionEvent event) {
        //Clears all fields
        txtNewCustName.clear();
        txtNewCustPhone.clear();
        txtNewCustAddress1.clear();
        txtNewCustAddress2.clear();
        txtNewCustZip.clear();
        cmbNewCustCountry.getSelectionModel().clearSelection();
        cmbNewCustCity.getSelectionModel().clearSelection();
        chkNewCustActive.setSelected(false);
        cmbNewCustCity.setDisable(true);
        cmbNewCustCity.setPromptText("Select a country");
    }

    @FXML
    void countryComboEventHandler(ActionEvent event) {
        //Shows & hides appropriate combo boxes depending on user selection
        if (cmbNewCustCountry.getValue() != null) {
            switch (cmbNewCustCountry.getValue()) {
                case "USA":
                    cmbNewCustCity.setItems(cityListUSA);
                    cmbNewCustCity.setDisable(false);
                    cmbNewCustCity.setPromptText("Select a city");
                    break;
                case "England":
                    cmbNewCustCity.setItems(cityListEngland);
                    cmbNewCustCity.setDisable(false);
                    cmbNewCustCity.setPromptText("Select a city");
                    break;
                default:
                    cmbNewCustCity.setDisable(true);
                    cmbNewCustCity.setPromptText("Select a country");
                    break;
            }
        }
    }

    //Shows an alert pop-up with no icon or header text
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
