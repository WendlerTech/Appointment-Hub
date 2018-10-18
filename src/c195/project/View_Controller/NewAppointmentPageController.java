package c195.project.View_Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField txtNewApptCustomer;
    @FXML
    private TextField txtNewApptContact;
    @FXML
    private DatePicker dateNewAppt;
    @FXML
    private ChoiceBox<Integer> cmbNewApptHrStart;
    @FXML
    private ChoiceBox<Integer> cmbNewApptMinStart;
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

    private Stage currentStage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList hourList = FXCollections.observableArrayList(
                8, 9, 10, 11, 12, 1, 2, 3, 4);
        ObservableList minuteList = FXCollections.observableArrayList(
                "00", "15", "30", "45");
        ObservableList locationList = FXCollections.observableArrayList(
                "London, England", "Phoenix, Arizona", "New York, New York");
        ObservableList apptTypeList = FXCollections.observableArrayList(
                "Consultation", "Accounting", "Sales", "Support");
        
        cmbNewApptHrStart.setItems(hourList);
        cmbNewApptHrEnd.setItems(hourList);
        cmbNewApptMinStart.setItems(minuteList);
        cmbNewApptMinEnd.setItems(minuteList);
        cmbNewApptLocation.setItems(locationList);
        cmbNewApptType.setItems(apptTypeList);
    }    
    
     public void setStage(Stage stage) {
        currentStage = stage;
    }
     
    @FXML
    void cancelNewApptButtonHandler(ActionEvent event) {
        //Closes window
        currentStage.close();
    }
    
    @FXML
    void clearNewApptButtonHandler(ActionEvent event) {
        //Clears all fields
        txtNewApptTitle.clear();
        cmbNewApptType.getSelectionModel().clearSelection();
        txtNewApptCustomer.clear();
        txtNewApptContact.clear();
        dateNewAppt.getEditor().clear(); //Clears textfield
        dateNewAppt.setValue(null); //Clears value
        cmbNewApptHrStart.getSelectionModel().clearSelection();
        cmbNewApptMinStart.getSelectionModel().clearSelection();
        cmbNewApptHrEnd.getSelectionModel().clearSelection();
        cmbNewApptMinEnd.getSelectionModel().clearSelection();
        cmbNewApptLocation.getSelectionModel().clearSelection();
        txtNewApptDescription.clear();
    }
    
    @FXML
    void viewCustNewApptButtonHandler(ActionEvent event) {
        //TO-DO: Open view customer page to selected customer
    }
    
    @FXML
    void saveNewAppointmentButtonHandler(ActionEvent event) {
        //TO-DO: Save new appointment
    }
}
