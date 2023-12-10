package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;
import application.*;

public class CustomerController {
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label customerEmailLabel;
    @FXML
    private Label customerPositionLabel;
    
    @FXML
    private Button updateProfileButton;

    private CustomerModel customer;
    private Main main;

//    public void initialize() {
//        // Initialize any required data or UI elements
//    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
        updateCustomerInfo();
    }

    private void updateCustomerInfo() {
        customerNameLabel.setText(customer.getName());
        customerEmailLabel.setText(customer.getEmail());
        customerPositionLabel.setText(customer.getPosition());
    }
    
    @FXML
    private void handleChangePassword() {
        // Show a dialog to update the customer's password
        boolean isPasswordUpdated = main.showUpdatePasswordDialog(customer);
        if (isPasswordUpdated) {
            showInfo("Password successfully changed.");
        }
    }

    @FXML
    private void handleUpdateProfile() {
        // Show a dialog to update the customer's profile
        CustomerModel updatedCustomer = main.showUpdateEmployeeDialog(customer);
        if (updatedCustomer != null) {
            customer = updatedCustomer;
            updateCustomerInfo();
        }
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setMain(Main main) {
        this.main = main;
    }
}

