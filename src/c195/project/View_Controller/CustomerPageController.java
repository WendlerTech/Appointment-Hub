package c195.project.View_Controller;

import c195.project.Address;
import c195.project.C195ProjectWendler;
import c195.project.Customer;
import c195.project.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
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
    private TableView<Customer> tblViewCustomers;
    @FXML
    private TableColumn<Customer, String> colCustName;
    @FXML
    private TableColumn<Address, String> colCustPhone;
    @FXML
    private TableColumn<Address, String> colCustCity;
    @FXML
    private TableColumn<Customer, Integer> colCustActive;
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
        ObservableList<Customer> custList = DatabaseHelper.getCustomerList();
        
        //TO-DO: Grab address list, create "CustomerTableRow" object that takes 
        //info from both lists & populate the Customer TableView with said object
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
            controller.setStage(stage, mainApp.getCurrentUserName());
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
    void updateCustomerButtonHandler(ActionEvent event) throws SQLException {
        
    }
    
    @FXML
    void deleteCustomerButtonHandler(ActionEvent event) {
        
    }
}
