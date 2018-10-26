package c195.project;

import static c195.project.DatabaseHelper.getConnection;
import c195.project.View_Controller.AppointmentsPageController;
import c195.project.View_Controller.CalendarPageController;
import c195.project.View_Controller.CustomerPageController;
import c195.project.View_Controller.DashboardPageController;
import c195.project.View_Controller.LoginPageController;
import c195.project.View_Controller.ReportsPageController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Nick Wendler
 */
public class C195ProjectWendler extends Application {

    private Stage primaryStage;
    private String currentUserName = "";
    private ResultSet resultSet = null;

    public C195ProjectWendler() {
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Wendler Tech");


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/LoginPage.fxml"));
        Parent root = loader.load();
        LoginPageController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.getIcons().add(new Image(C195ProjectWendler.class.getResourceAsStream("View_Controller/Media/W Icon.png")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void loginButton(String username, String password) {
        //Verifies login data. Cast as binary makes password case-sensative.
        String queryString = "SELECT * FROM user WHERE userName = ? AND "
                + "CAST(password AS BINARY) = ?;";
        
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement statement = conn.prepareStatement(queryString);) {
            statement.setString(1, username);
            statement.setString(2, password);
            
            resultSet = statement.executeQuery();
            
            //If any result is returned, username & password match
            if (resultSet.next()) {
                currentUserName = username;
                openDashboard();                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                        "Invalid username or password.");
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.showAndWait();
            }            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());
        }
    }
    
    public void openDashboard() {
         try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/DashboardPage.fxml"));
            Parent root = loader.load();
            DashboardPageController controller = loader.getController();
            controller.setMainApp(this);
            controller.setUserNameLabel(currentUserName);
            
            controller.setStage(primaryStage);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());
        }
    }
    
    public void dashOpenAppointmentsButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/AppointmentsPage.fxml"));
            Parent root = loader.load();
            AppointmentsPageController controller = loader.getController();
            controller.setMainApp(this);
            
            controller.setStage(primaryStage);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }
    
    public void dashOpenCustomersButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/CustomerPage.fxml"));
            Parent root = loader.load();
            CustomerPageController controller = loader.getController();
            controller.setMainApp(this);
            
            controller.setStage(primaryStage);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }
    
    public void dashOpenCalendarButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/CalendarPage.fxml"));
            Parent root = loader.load();
            CalendarPageController controller = loader.getController();
            controller.setMainApp(this);
            
            controller.setStage(primaryStage);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }
    
    public void dashOpenReportsButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(C195ProjectWendler.class.getResource("View_Controller/ReportsPage.fxml"));
            Parent root = loader.load();
            ReportsPageController controller = loader.getController();
            controller.setMainApp(this);
            
            controller.setStage(primaryStage);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.show();            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error, namely: " + e.getMessage());            
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        launch(args);
    }

}
