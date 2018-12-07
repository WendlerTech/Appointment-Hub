package c195.project.View_Controller;

import c195.project.Appointment;
import c195.project.C195ProjectWendler;
import c195.project.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private TableColumn<Appointment, String> colApptConsultant;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private static ObservableList<Appointment> apptList = FXCollections.observableArrayList();

    private static boolean dataWasUpdated = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
        try {
            populateTableData();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void populateTableData() throws SQLException {
        if (mainApp.isInitialDataChanged()) {
            apptList = DatabaseHelper.getAppointmentList();
        } else {
            apptList = mainApp.getAppointmentList();
        }

        tblViewAppointments.setItems(apptList);

        colApptTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        colApptContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colApptType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        colApptLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        colApptConsultant.setCellValueFactory(cellData -> new SimpleStringProperty(capitalizeFirstLetter(cellData.getValue().getCreatedBy())));
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
            NewAppointmentPageController.setOpenedFromCalendar(false);
            stage.showAndWait();
            refreshTable();
        } catch (IOException ex) {
            Logger.getLogger(AppointmentsPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void updateAppointmentButtonHandler(ActionEvent event) {
        if (tblViewAppointments.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/ViewUpdateAppointmentPage.fxml"));
                Parent root = loader.load();
                ViewUpdateAppointmentPageController controller = loader.getController();
                controller.setMainApp(mainApp);

                Appointment apptToUpdate = null;
                for (Appointment appt : apptList) {
                    if (appt.getAppointmentID() == tblViewAppointments.getSelectionModel().getSelectedItem().getAppointmentID()) {
                        apptToUpdate = appt;
                    }
                }

                Stage stage = new Stage();
                controller.setStage(stage, apptToUpdate, mainApp.getCurrentUser());
                stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));

                stage.setScene(new Scene(root));
                stage.setTitle("View or update an existing appointment");
                ViewUpdateAppointmentPageController.setOpenedFromCalendar(false);
                stage.showAndWait();
                refreshTable();
            } catch (IOException ex) {
                Logger.getLogger(AppointmentsPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void deleteAppointmentButtonHandler(ActionEvent event) throws SQLException {
        if (tblViewAppointments.getSelectionModel().getSelectedItem() != null) {
            Appointment apptToDelete = tblViewAppointments.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete "
                    + "the appointment titled \"" + apptToDelete.getTitle() + "\"?"
                    + "\nThis cannot be undone.", ButtonType.YES, ButtonType.NO);
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                if (DatabaseHelper.deleteAppointment(apptToDelete.getAppointmentID())) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Appointment has been deleted successfully.");
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    dataWasUpdated = true;
                    mainApp.setInitialDataChanged(dataWasUpdated);
                    refreshTable();
                }
            }
        }
    }

    //Re-populates table if changes are detected
    private void refreshTable() {
        if (dataWasUpdated) {
            try {
                tblViewAppointments.getItems().clear();
                populateTableData();
                dataWasUpdated = false;
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentsPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public static ObservableList getApptList() {
        return apptList;
    }

    //Allows table to not re-populate if nothing has changed for improved runtime
    public static boolean isDataWasUpdated() {
        return dataWasUpdated;
    }

    public static void setDataWasUpdated(boolean dataWasUpdated) {
        AppointmentsPageController.dataWasUpdated = dataWasUpdated;
    }
}
