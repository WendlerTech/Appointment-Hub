package c195.project.View_Controller;

import c195.project.Address;
import c195.project.C195ProjectWendler;
import c195.project.Customer;
import c195.project.CustomerTableRow;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class CustomerPageController implements Initializable {

    @FXML
    private TableView<CustomerTableRow> tblViewCustomers;
    @FXML
    private TableColumn<CustomerTableRow, String> colCustName;
    @FXML
    private TableColumn<CustomerTableRow, String> colCustPhone;
    @FXML
    private TableColumn<CustomerTableRow, String> colCustCity;
    @FXML
    private TableColumn<CustomerTableRow, Boolean> colCustActive;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private static boolean dataWasUpdated = false;

    private final ObservableList<CustomerTableRow> custTableRowList = FXCollections.observableArrayList();
    private ObservableList<Address> addrList = FXCollections.observableArrayList();
    private ObservableList<Customer> custList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateTableData();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStage(Stage stage) {
        currentStage = stage;
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }

    public void populateTableData() throws SQLException {
        addrList = DatabaseHelper.getAddressList();
        custList = DatabaseHelper.getCustomerList();

        tblViewCustomers.setItems(custTableRowList);

        //Functional operation uses lambda for readability
        custList.stream().map((customerList) -> {
            //Creates new custRow object on each iteration, assigns data
            CustomerTableRow custRow = new CustomerTableRow();
            int addressId = customerList.getAddressID();

            for (Address addr : addrList) {
                if (addr.getAddressID() == addressId) {
                    //Address record matches customer's
                    custRow.setAddressId(addr.getAddressID());
                    custRow.setPhoneNum(addr.getPhoneNumber());
                    custRow.setCity(DatabaseHelper.getCityName(addr.getCityID()));
                    break; //Break loop once match is found
                }
            }
            custRow.setCustName(customerList.getCustName());
            custRow.setCustIsActive(customerList.isIsActive() > 0);
            custRow.setCustId(customerList.getCustID());

            return custRow;
        }).forEachOrdered((custRow) -> {
            //Adds object to new list, repeats until all custs have been cycled
            custTableRowList.add(custRow);
        });

        colCustName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustName()));
        colCustPhone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNum()));
        colCustCity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
        colCustActive.setCellValueFactory(cellData -> cellData.getValue().getCustIsActive());
        colCustActive.setCellFactory(CheckBoxTableCell.forTableColumn(colCustActive));
        colCustName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustName()));
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
            refreshTable();
        } catch (IOException ex) {
            Logger.getLogger(CustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void updateCustomerButtonHandler(ActionEvent event) throws SQLException {
        if (tblViewCustomers.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/UpdateCustomerPage.fxml"));
                Parent root = loader.load();
                UpdateCustomerPageController controller = loader.getController();

                //Turns selected item into an object
                CustomerTableRow selectedCustomer = tblViewCustomers.getSelectionModel().getSelectedItem();
                Customer customerToUpdate = null;
                Address addrToUpdate = null;

                //Searches pre-queried lists for selected cust, optimizing load time
                for (Customer cust : custList) {
                    if (cust.getCustID() == selectedCustomer.getCustId()) {
                        customerToUpdate = cust;
                        break;
                    }
                }
                for (Address addr : addrList) {
                    if (addr.getAddressID() == selectedCustomer.getAddressId()) {
                        addrToUpdate = addr;
                        break;
                    }
                }

                //Passes newly created (& populated) objects to update customer page
                Stage stage = new Stage();
                controller.setStage(stage, customerToUpdate, addrToUpdate,
                        mainApp.getCurrentUserName());
                stage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));

                stage.setScene(new Scene(root));
                stage.setTitle("View or update an existing customer");
                stage.showAndWait();
                refreshTable();
            } catch (IOException ex) {
                Logger.getLogger(CustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Re-populates table if changes are detected
    private void refreshTable() {
        if (dataWasUpdated) {
            try {
                tblViewCustomers.getItems().clear();
                populateTableData();
                dataWasUpdated = false;
            } catch (SQLException ex) {
                Logger.getLogger(CustomerPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void deleteCustomerButtonHandler(ActionEvent event) throws SQLException {
        if (tblViewCustomers.getSelectionModel().getSelectedItem() != null) {
            CustomerTableRow custToDelete = tblViewCustomers.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete " + custToDelete.getCustName()
                    + " from " + custToDelete.getCity() + "?"
                    + "\nThis cannot be undone!", ButtonType.YES,
                    ButtonType.NO);
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                if (DatabaseHelper.deleteCustomer(custToDelete.getCustId(), custToDelete.getAddressId())) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Customer has been deleted successfully.");
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    dataWasUpdated = true;
                    refreshTable();
                }
            }
        }
    }

    //Allows table to not re-populate if nothing has changed for improved runtime
    public static boolean isDataWasUpdated() {
        return dataWasUpdated;
    }

    public static void setDataWasUpdated(boolean dataWasUpdated) {
        CustomerPageController.dataWasUpdated = dataWasUpdated;
    }
}
