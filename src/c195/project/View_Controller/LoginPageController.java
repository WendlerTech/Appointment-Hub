package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import c195.project.DatabaseHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML
    private Button btnLoginRegister;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private String username, password;
    private String newUsername, newPassword;

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

        if (username.length() == 0 || password.length() == 0) {
            showErrorAlert("Please enter your credentials. "
                    + "\nIf you're a first time user, please enter your "
                    + "desired username & password, then click register.");
        } else {
            if (matchRegex(username)) {
                mainApp.loginButton(username, password, false);
            } else {
                showErrorAlert("Invalid character in username.");
            }
        }
    }

    @FXML
    void registerButtonHandler(ActionEvent event) throws SQLException {
        newUsername = txtLoginUsername.getText();
        newPassword = txtLoginPassword.getText();

        if (newUsername.length() == 0 || newPassword.length() == 0) {
            showErrorAlert("Fields can't be blank."
                    + "\nIf you're a first time user, please enter your "
                    + "desired username & password, then click register.");
        } else {
            if (matchRegex(newUsername)) {
                if (newUsername.length() == 0) {
                    //Username is blank
                    showErrorAlert("Your username can't be blank.");
                } else if (newPassword.length() < 4) {
                    //Password isn't long enough
                    showErrorAlert("Your password must be at least 4 characters.");
                } else {
                    if (!DatabaseHelper.checkIfUserExists(newUsername)) {
                        //Username isn't taken
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                "Are you sure you wish to register as " + newUsername
                                + "?", ButtonType.YES, ButtonType.NO);
                        alert.initStyle(StageStyle.UTILITY);
                        alert.setHeaderText(null);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            DatabaseHelper.addNewUser(newUsername, newPassword);
                            mainApp.loginButton(newUsername, newPassword, true);
                        }
                    } else {
                        showErrorAlert("Username is already taken.");
                    }
                }
            } else {
                //Username contains illegal characters
                showErrorAlert("Invalid character. Please use only letters "
                        + "in your username.");
            }
        }
    }

    public void showErrorAlert(String message) {
        //Shows an alert pop-up with no icon or header text
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public boolean matchRegex(String validString) {
        //Returns true if string contains only letters
        return validString.matches("[A-Za-z]*");
    }

    public void setMainApp(C195ProjectWendler mainApp) {
        this.mainApp = mainApp;
    }
}
