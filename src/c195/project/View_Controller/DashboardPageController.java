package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class DashboardPageController implements Initializable {

    @FXML
    private Label lblDashWelcome;
    @FXML
    private Button btnDashAppt;
    @FXML
    private Button btnDashCust;
    @FXML
    private Button btnDashCalendar;
    @FXML
    private Button btnDashReports;

    private C195ProjectWendler mainApp;
    private Stage currentStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //Updates welcome label with the user's name
    public void setUserNameLabel(String username) {
        String formattedUsername = capitalizeFirstLetter(username);
        //Moves username to next line if user has a long name (for formatting)
        if (formattedUsername.length() > 10) {
            lblDashWelcome.setText("Welcome, \n" + formattedUsername + ".");
        } else if (formattedUsername.length() > 0) {
            lblDashWelcome.setText("Welcome, " + formattedUsername + ".");
        } else {
            //No username entered
            lblDashWelcome.setText("Welcome.");
        }
    }

    @FXML
    void openAppointmentsButtonHandler(ActionEvent event) {
        mainApp.dashOpenAppointmentsButton();
    }

    @FXML
    void openCustomersButtonHandler(ActionEvent event) {
        mainApp.dashOpenCustomersButton();
    }

    @FXML
    void openCalendarButtonHandler(ActionEvent event) {
        mainApp.dashOpenCalendarButton();
    }

    @FXML
    void openReportsButtonHandler(ActionEvent event) {
        mainApp.dashOpenReportsButton();
    }

    @FXML
    void logOutButtonHandler(ActionEvent event) throws Exception {
        mainApp.start(currentStage);
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
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
