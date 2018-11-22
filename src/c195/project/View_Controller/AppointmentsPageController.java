package c195.project.View_Controller;

import c195.project.Appointment;
import c195.project.C195ProjectWendler;
import c195.project.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class AppointmentsPageController implements Initializable {

    @FXML
    private TableView<Appointment> tblViewAppointments;
    @FXML
    private TableColumn<Appointment, String> colApptTitle;
    @FXML
    private TableColumn<Appointment, String> colApptContact;
    @FXML
    private TableColumn<Appointment, String> colApptType;
    @FXML
    private TableColumn<Appointment, String> colApptLocation;
    @FXML
    private TableColumn<Appointment, String> colApptDate;
    @FXML
    private RadioButton radApptSortWeek;
    @FXML
    private ToggleGroup grpSortApptBy;
    @FXML
    private RadioButton radApptSortMonth;
    @FXML
    private Button btnApptDelete;
    @FXML
    private Button btnApptViewUpdate;
    @FXML
    private Button btnApptAdd;
    @FXML
    private Button btnApptBack;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private ObservableList<Appointment> apptList = FXCollections.observableArrayList();

    private static boolean dataWasUpdated = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateTableData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }

    public void populateTableData() throws SQLException {
        apptList = DatabaseHelper.getAppointmentList();

        tblViewAppointments.setItems(apptList);

        colApptTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        colApptContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colApptType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        colApptLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        colApptDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));
    }

    @FXML
    void apptGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }

    @FXML
    void addAppointmentButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/NewAppointmentPage.fxml"));
            Parent root = loader.load();
            NewAppointmentPageController controller = loader.getController();
            controller.setMainApp(mainApp);

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));

            stage.setScene(new Scene(root));
            stage.setTitle("Add a new appointment");
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void updateAppointmentButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/ViewUpdateAppointment.fxml"));
            Parent root = loader.load();
            ViewUpdateAppointmentPageController controller = loader.getController();
            controller.setMainApp(mainApp);

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));

            stage.setScene(new Scene(root));
            stage.setTitle("View or update an existing appointment");
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Re-populates table if changes are detected
    private void refreshTable() {
        if (dataWasUpdated) {
            try {
                tblViewAppointments.getItems().clear();
                populateTableData();
                dataWasUpdated = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Allows table to not re-populate if nothing has changed for improved runtime
    public static boolean isDataWasUpdated() {
        return dataWasUpdated;
    }

    public static void setDataWasUpdated(boolean dataWasUpdated) {
        AppointmentsPageController.dataWasUpdated = dataWasUpdated;
    }
}
