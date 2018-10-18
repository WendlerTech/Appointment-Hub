package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

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
    private TableView<?> tblReports;
    @FXML
    private Button btnCustBack;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> monthList = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", 
                "December");
        ObservableList apptTypeList = FXCollections.observableArrayList(
                "Consultation", "Accounting", "Sales", "Support");
        
        cmbAvailableReports.getItems().add("Customers By City");
        cmbAvailableReports.getItems().add("Consultant Schedule");
        cmbAvailableReports.getItems().add("Appointment Types By Month");
        
        cmbReportsMonth.setItems(monthList);
        cmbReportsApptType.setItems(apptTypeList);
    }    
    
            
     public void setStage(Stage stage) {
        currentStage = stage;
    }
     
    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }     
    
    @FXML
    void custGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }
    
    @FXML
    void reportsComboBoxEventHandler(ActionEvent event) {
        if (cmbAvailableReports.getValue().equals("Appointment Types By Month")) {
        cmbReportsApptType.setVisible(true);
        cmbReportsMonth.setVisible(true);            
        } else {
        cmbReportsApptType.setVisible(false);
        cmbReportsMonth.setVisible(false);            
        }
    }
}
