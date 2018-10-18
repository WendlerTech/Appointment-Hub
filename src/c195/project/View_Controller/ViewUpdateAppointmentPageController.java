package c195.project.View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class ViewUpdateAppointmentPageController implements Initializable {

    @FXML
    private TextField txtViewApptTitle;
    @FXML
    private ComboBox<?> cmbViewApptType;
    @FXML
    private TextField txtViewApptCustomer;
    @FXML
    private TextField txtViewApptContact;
    @FXML
    private DatePicker dateViewAppt;
    @FXML
    private ChoiceBox<?> cmbViewApptHrStart;
    @FXML
    private ChoiceBox<?> cmbViewApptMinStart;
    @FXML
    private ChoiceBox<?> cmbViewApptHrEnd;
    @FXML
    private ChoiceBox<?> cmbViewApptMinEnd;
    @FXML
    private ChoiceBox<?> cmbViewApptLocation;
    @FXML
    private TextArea txtViewApptDescription;
    @FXML
    private Button btnSaveViewAppt;
    @FXML
    private Button btnClearViewAppt;
    @FXML
    private Button btnCancelViewAppt;
    @FXML
    private Button btnViewApptViewCust;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}