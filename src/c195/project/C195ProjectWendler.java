package c195.project;

import c195.project.View_Controller.AppointmentsPageController;
import c195.project.View_Controller.CalendarPageController;
import c195.project.View_Controller.CustomerPageController;
import c195.project.View_Controller.DashboardPageController;
import c195.project.View_Controller.LoginPageController;
import c195.project.View_Controller.ReportsPageController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Nick Wendler
 */
public class C195ProjectWendler extends Application {

    private Stage primaryStage;
    private String currentUserName = "";

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
        //TO-DO: Verify login data
        currentUserName = username;
        openDashboard();
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
        DatabaseHelper.openConnection();
        launch(args);
        DatabaseHelper.closeConnection();
    }

}