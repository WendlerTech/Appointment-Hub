package c195.project.View_Controller;

import c195.project.Appointment;
import c195.project.C195ProjectWendler;
import c195.project.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class CalendarPageController implements Initializable {

    @FXML
    private TableView<Appointment> tblViewAppointments;
    @FXML
    private TableColumn<Appointment, String> calColTitle;
    @FXML
    private TableColumn<Appointment, String> calColContact;
    @FXML
    private TableColumn<Appointment, String> calColType;
    @FXML
    private TableColumn<Appointment, String> calColLocation;
    @FXML
    private TableColumn<Appointment, String> calColDate;
    @FXML
    private RadioButton radApptSortWeek;
    @FXML
    private ToggleGroup grpSortApptBy;
    @FXML
    private RadioButton radApptSortMonth;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private static ObservableList<Appointment> apptList;
    private ObservableList<Appointment> weeklyApptList, monthlyApptList;
    private static boolean dataWasUpdated = false;
    private boolean showWeeklyData = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grpSortApptBy = new ToggleGroup();
        radApptSortWeek.setToggleGroup(grpSortApptBy);
        radApptSortMonth.setToggleGroup(grpSortApptBy);

        //Toggle group listens for change, refreshes table based on selection. Lambda used for readability.
        grpSortApptBy.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            if (grpSortApptBy.getSelectedToggle() != null) {
                RadioButton selectedBtn = (RadioButton) grpSortApptBy.getSelectedToggle();
                showWeeklyData = !selectedBtn.getText().equals("Next month");
                populateTableData();
            }
        });

        grpSortApptBy.selectToggle(radApptSortWeek);
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void displayWeeklyData() {
        weeklyApptList = FXCollections.observableArrayList();
        DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate apptDate;
        Date date = new Date();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt;
        LocalDate nextweek = currentDate.plusWeeks(1);
        for (Appointment appt : apptList) {
            try {
                //Converts string into date object
                date = formatDay.parse(appt.getStartTime());
            } catch (ParseException ex) {
                Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Converts date object into LocalDate
            Instant instant = date.toInstant();
            zdt = ZonedDateTime.ofInstant(instant, zoneId);
            apptDate = LocalDate.from(zdt);

            if ((apptDate.isAfter(currentDate) || apptDate.isEqual(currentDate)) && apptDate.isBefore(nextweek)) {
                weeklyApptList.add(appt);
            }
        }

        tblViewAppointments.getItems().clear();
        tblViewAppointments.setItems(weeklyApptList);
    }

    public void displayMonthlyData() {
        monthlyApptList = FXCollections.observableArrayList();
        DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate apptDate;
        Date date = new Date();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt;
        for (Appointment appt : apptList) {
            try {
                //Converts string into date object
                date = formatDay.parse(appt.getStartTime());
            } catch (ParseException ex) {
                Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Converts date object into LocalDate
            Instant instant = date.toInstant();
            zdt = ZonedDateTime.ofInstant(instant, zoneId);
            apptDate = LocalDate.from(zdt);

            if (apptDate.getMonth().equals(currentDate.getMonth())) {
                monthlyApptList.add(appt);
            }
        }

        tblViewAppointments.getItems().clear();
        tblViewAppointments.setItems(monthlyApptList);
    }

    public void populateTableData() {
        try {
            apptList = DatabaseHelper.getAppointmentList();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (showWeeklyData) {
            displayWeeklyData();
        } else {
            displayMonthlyData();
        }

        calColTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        calColContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        calColType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        calColLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        calColDate.setCellValueFactory(cellData -> new SimpleStringProperty(formatColDate(cellData.getValue().getStartTime())));
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }

    //Re-populates table if changes are detected
    private void refreshTable() {
        if (dataWasUpdated) {
            tblViewAppointments.getItems().clear();
            populateTableData();
            dataWasUpdated = false;
        }
    }

    @FXML
    void calGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }

    @FXML
    void deleteAppointmentCalButtonHandler(ActionEvent event) throws SQLException {
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
                    refreshTable();
                }
            }
        }
    }

    @FXML
    void updateAppointmentCalButtonHandler(ActionEvent event) {
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
                ViewUpdateAppointmentPageController.setOpenedFromCalendar(true);
                stage.showAndWait();
                refreshTable();
            } catch (IOException ex) {
                Logger.getLogger(AppointmentsPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void addAppointmentCalButtonHandler(ActionEvent event) {
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
            NewAppointmentPageController.setOpenedFromCalendar(true);
            stage.showAndWait();
            refreshTable();
        } catch (IOException ex) {
            Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String formatColDate(String dateToFormat) {
        //Takes in string date
        DateFormat formatFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("EEE, MMM d, h:mm a");
        Date date = new Date();
        try {
            //Converts string to date object
            date = formatFullDate.parse(dateToFormat);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Formats date object, returns as string
        return format.format(date);
    }

    public static ObservableList getApptList() {
        return apptList;
    }

    //Allows table to not re-populate if nothing has changed for improved runtime
    public static boolean isDataWasUpdated() {
        return dataWasUpdated;
    }

    public static void setDataWasUpdated(boolean dataWasUpdated) {
        CalendarPageController.dataWasUpdated = dataWasUpdated;
    }
}
