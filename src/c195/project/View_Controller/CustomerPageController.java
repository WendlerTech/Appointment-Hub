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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class CustomerPageController implements Initializable {

    @FXML
    private TableView<?> tblViewCustomers;
    @FXML
    private TableColumn<?, ?> colCustName;
    @FXML
    private TableColumn<?, ?> colCustPhone;
    @FXML
    private TableColumn<?, ?> colCustAddress;
    @FXML
    private TableColumn<?, ?> colCustCity;
    @FXML
    private TableColumn<?, ?> colCustZip;
    @FXML
    private TableColumn<?, ?> colCustActive;
    @FXML
    private Label lblCustHeader;
    @FXML
    private Button btnCustAdd;
    @FXML
    private Button btnCustUpdate;
    @FXML
    private Button btnCustDelete;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    
    /**
     * Initializes the controller class.
     */
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
    void custGoBackButtonHandler(ActionEvent event) throws IOException {
        mainApp.openDashboard();
    }
    
    @FXML
    void addCustomerButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/NewCustomerPage.fxml"));
            Parent root = loader.load();
            NewCustomerPageController controller = loader.getController();
            
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));
            
            stage.setScene(new Scene(root));
            stage.setTitle("Add a new customer");
            stage.showAndWait();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }
    
    @FXML
    void updateCustomerButtonHandler(ActionEvent event) {
        
    }
    
    @FXML
    void deleteCustomerButtonHandler(ActionEvent event) {
        
    }
}
