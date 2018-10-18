package c195.project.View_Controller;

import c195.project.Address;
import c195.project.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

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
    @FXML
    private Button btnAddCustomer;
    @FXML
    private Button btnNewCustClear;
    @FXML
    private Button btnNewCustCancel;

    private Stage currentStage;
    Customer newCust;
    Address custAddress;
        
    private ObservableList cityListUSA = FXCollections.observableArrayList(
                "Phoenix", "New York", "Seattle", "Houston", "Denver");
    private ObservableList cityListEngland = FXCollections.observableArrayList(
                "London", "Manchaster", "Liverpool", "Birmingham");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Auto-adds hyphens to phone number field
        txtNewCustPhone.setTextFormatter(new TextFormatter<>(change ->{
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
    }    
    
     public void setStage(Stage stage) {
        currentStage = stage;
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
        
        custAddress.setAddress(address1);
        custAddress.setAddress2(address2);
        custAddress.setZipCode(zipCode);
        custAddress.setPhoneNumber(phoneNum);
        newCust.setCustName(custName);
        newCust.setIsActive(isActive);
    }
    
    @FXML
    void cancelNewCustButtonHandler(ActionEvent event) {
        //Closes window
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
}
