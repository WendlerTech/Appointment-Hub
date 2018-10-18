package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class CalendarPageController implements Initializable {

    @FXML
    private TableView<?> tblViewAppointments;
    @FXML
    private RadioButton radApptSortWeek;
    @FXML
    private ToggleGroup grpSortApptBy;
    @FXML
    private RadioButton radApptSortMonth;
    @FXML
    private Button btnCalDelete;
    @FXML
    private Button btnCalViewUpdate;
    @FXML
    private Button btnCalAdd;
    @FXML
    private Button btnCalBack;
    
    private C195ProjectWendler mainApp;
    private Stage currentStage;
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
     public void setStage(Stage stage) {
        currentStage = stage;
    }
     
    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }     
    
    @FXML
    void calGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }
    
    @FXML
    void addAppointmentButtonHandler(ActionEvent event) {
         try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/NewAppointmentPage.fxml"));
            Parent root = loader.load();
            NewAppointmentPageController controller = loader.getController();
            
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));
            
            stage.setScene(new Scene(root));
            stage.setTitle("Add a new appointment");
            stage.showAndWait();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }
}
