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
    private ComboBox<String> cmbReportsConsultantName;
    @FXML
    private ComboBox<String> cmbReportsCountry;
    @FXML
    private ComboBox<String> cmbReportsCity;
    @FXML
    private TableView<?> tblReports;
    @FXML
    private Button btnCustBack;

    private C195ProjectWendler mainApp;
    private Stage currentStage;

    private ObservableList cityListEngland = FXCollections.observableArrayList(
            "London", "Manchester", "Liverpool", "Birmingham");
    private ObservableList cityListUSA = FXCollections.observableArrayList(
            "Phoenix", "New York", "Seattle", "Houston", "Denver");

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
        ObservableList countryList = FXCollections.observableArrayList(
                "England", "USA");

        cmbAvailableReports.getItems().add("Customers By City");
        cmbAvailableReports.getItems().add("Consultant Schedule");
        cmbAvailableReports.getItems().add("Appointment Types By Month");

        cmbReportsCity.setDisable(true);
        cmbReportsCity.setPromptText("Choose a country");
        cmbReportsCountry.setItems(countryList);
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
        //Shows & hides appropriate combo boxes depending on user selection
        switch (cmbAvailableReports.getValue()) {
            case "Customers By City":
                cmbReportsCity.setVisible(true);
                cmbReportsCountry.setVisible(true);
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsConsultantName.setVisible(false);
                break;
            case "Consultant Schedule":
                cmbReportsConsultantName.setVisible(true);
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
                break;
            case "Appointment Types By Month":
                cmbReportsApptType.setVisible(true);
                cmbReportsMonth.setVisible(true);
                cmbReportsConsultantName.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
                break;
            default:
                cmbReportsApptType.setVisible(false);
                cmbReportsMonth.setVisible(false);
                cmbReportsConsultantName.setVisible(false);
                cmbReportsCountry.setVisible(false);
                cmbReportsCity.setVisible(false);
        }
    }

    @FXML
    void countryComboEventHandler(ActionEvent event) {
        //Displays cities based on which country the user has selected
        if (cmbReportsCountry.getValue() != null) {
            switch (cmbReportsCountry.getValue()) {
                case "USA":
                    cmbReportsCity.setItems(cityListUSA);
                    cmbReportsCity.setDisable(false);
                    cmbReportsCity.setPromptText("Select a city");
                    break;
                case "England":
                    cmbReportsCity.setItems(cityListEngland);
                    cmbReportsCity.setDisable(false);
                    cmbReportsCity.setPromptText("Select a city");
                    break;
                default:
                    cmbReportsCity.setDisable(true);
                    cmbReportsCity.setPromptText("Choose a country");
                    break;
            }
        }
    }
}
