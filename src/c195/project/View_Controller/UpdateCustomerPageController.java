package c195.project.View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    private ComboBox<?> cmbUpdateCustCountry;
    @FXML
    private ComboBox<?> cmbUpdateCustCity;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
