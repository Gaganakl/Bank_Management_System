package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import models.Customer;
import controllers.UpdateCustomerController;
import controllers.UpdatePasswordController;
import controllers.SignupController;
import models.Dao;
import java.io.IOException;
import controllers.CustomerController;
import controllers.LoginController;
import javafx.scene.layout.VBox;

public class Main extends Application {

    private Stage primaryStage;
    private Dao dao; // Declare the Dao instance variable at the class level

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dao = new Dao(); // Initialize the Dao instance
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();

            // Get the LoginController and set the main instance
            LoginController controller = loader.getController();
            controller.setMain(this);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bank Management System");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showCustomerDashboard(Customer employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/CustomerDashboardView.fxml"));
            Parent root = loader.load();

            // Get the EmployeeController
            CustomerController controller = loader.getController();
            controller.setCustomer(employee);
            controller.setMain(this);

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Customer showUpdateEmployeeDialog(Customer employee) {
        try {
            // Load the FXML for the update employee dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/views/UpdateCustomerView.fxml"));
            Parent root = loader.load();

            // Set the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Employee");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Get the controller for the update employee dialog
            UpdateCustomerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(employee);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (controller.isUpdateClicked()) {
                Customer updatedEmployee = controller.getUpdatedCustomer();
                // Call the updateEmployee method from Dao class
                Dao dao = new Dao();
                dao.updateCustomer(updatedEmployee);
                return updatedEmployee;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean showUpdatePasswordDialog(Customer employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdatePasswordView.fxml"));
            VBox page = (VBox) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Change Password");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UpdatePasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(employee);
            controller.setDao(dao);

            dialogStage.showAndWait();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void showSignupDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignupView.fxml"));
            Parent root = loader.load();

            SignupController controller = loader.getController();
            controller.setMain(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sign Up");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
        
    }
}
