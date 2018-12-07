package c195.project.View_Controller;

import c195.project.C195ProjectWendler;
import c195.project.DatabaseHelper;
import c195.project.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
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
    private TextField txtLoginUsername;
    @FXML
    private PasswordField txtLoginPassword;
    @FXML
    private Label lblLoginUsername;
    @FXML
    private Label lblLoginPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnLoginRegister;

    private C195ProjectWendler mainApp;
    private Stage currentStage;
    private User user;
    private String username, password;
    private String newUsername, newPassword;
    private final Locale argentineLocale = new Locale("es", "AR");
    private ResourceBundle languageBundle = null;

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

    @FXML
    void loginButtonHandler(ActionEvent event) {
        username = txtLoginUsername.getText();
        password = txtLoginPassword.getText();

        if (username.length() == 0 || password.length() == 0) {
            showErrorAlert(languageBundle.getString("blankFields"));
        } else {
            if (matchRegex(username)) {
                user = DatabaseHelper.userLogin(username, password);
                if (user != null) {
                    mainApp.loginButton(user);
                } else {
                    showErrorAlert(languageBundle.getString("invalidPass"));
                }
            } else {
                showErrorAlert(languageBundle.getString("invalidChar"));
            }
        }
    }

    @FXML
    void registerButtonHandler(ActionEvent event) throws SQLException {
        newUsername = txtLoginUsername.getText();
        newPassword = txtLoginPassword.getText();

        if (newUsername.length() == 0 || newPassword.length() == 0) {
            showErrorAlert(languageBundle.getString("blankFields"));
        } else {
            if (matchRegex(newUsername)) {
                if (newPassword.length() < 4) {
                    //Password isn't long enough
                    showErrorAlert(languageBundle.getString("shortPassword"));
                } else {
                    if (!DatabaseHelper.checkIfUserExists(newUsername)) {
                        //Username isn't taken
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                languageBundle.getString("registerConfirm") + newUsername
                                + "?", ButtonType.YES, ButtonType.NO);
                        alert.initStyle(StageStyle.UTILITY);
                        alert.setHeaderText(null);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            user = DatabaseHelper.addNewUser(newUsername, newPassword);
                            if (user != null) {
                                mainApp.loginButton(user);
                            } else {
                                showErrorAlert(languageBundle.getString("errorSavingData"));
                            }
                        }
                    } else {
                        showErrorAlert(languageBundle.getString("usernameTaken"));
                    }
                }
            } else {
                //Username contains illegal characters
                showErrorAlert(languageBundle.getString("invalidChar"));
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

    public void checkLocale() {
        if (Locale.getDefault() == Locale.US) {
            languageBundle = ResourceBundle.getBundle("c195.project.Language_Bundles/Bundle_en_US");
        } else if (Locale.getDefault().equals(argentineLocale)) {
            languageBundle = ResourceBundle.getBundle("c195.project.Language_Bundles/Bundle_es_AR");
        }

        lblLoginUsername.setText(languageBundle.getString("lblUsername"));
        lblLoginPassword.setText(languageBundle.getString("lblPassword"));
        btnLoginRegister.setText(languageBundle.getString("btnRegister"));
        btnLogin.setText(languageBundle.getString("btnLogin"));
    }
}
