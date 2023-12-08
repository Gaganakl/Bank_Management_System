package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import models.Customer;
import models.Dao;
import utils.BcryptHash;

public class UpdatePasswordController {

    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private Stage dialogStage;
    private Customer customer;
    private Dao dao;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleUpdatePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (dao.isPasswordCorrect(customer.getUserId(), currentPassword)) {
            if (newPassword.equals(confirmPassword)) {
                String hashedNewPassword = BcryptHash.hashPassword(newPassword);
                if (dao.updateUserPassword(customer.getUserId(), hashedNewPassword)) {
                    // Show success message
                    showInfo("Password updated successfully.");
                    dialogStage.close();
                } else {
                    // Show error message
                    showError("Failed to update the password.");
                }
            } else {
                // Show error message
                showError("New password and confirmation do not match.");
            }
        } else {
            // Show error message
            showError("New password and confirmation do not match.");
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
