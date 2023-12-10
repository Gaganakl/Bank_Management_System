package controllers;

//import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.CustomerModel;

public class UpdateCustomerController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField positionField;

    private Stage dialogStage;
    private CustomerModel customer;
    private boolean updateClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        positionField.setText(customer.getPosition());
    }

    public boolean isUpdateClicked() {
        return updateClicked;
    }

    public CustomerModel getUpdatedCustomer() {
        return customer;
    }

    @FXML
    private void handleUpdate() {
        customer.setName(nameField.getText());
        customer.setEmail(emailField.getText());
        customer.setPosition(positionField.getText());
        updateClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
