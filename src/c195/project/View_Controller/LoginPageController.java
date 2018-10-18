package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nick Wendler
 */
public class LoginPageController implements Initializable {

    @FXML
    private Label lblLoginUsername;
    @FXML
    private Label lblLoginPassword;
    @FXML
    private TextField txtLoginUsername;
    @FXML
    private PasswordField txtLoginPassword;
    @FXML
    private Button btnLogin;
    
    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private String username, password;

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
    
    @FXML
    void loginButtonHandler(ActionEvent event) {
        username = txtLoginUsername.getText();
        password = txtLoginPassword.getText();
        mainApp.loginButton(username, password);
    }
    
    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }
    
}
