package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

//import application.*;

public class AdminController {
    @FXML
    private TableView<CustomerModel> customerTable;
    @FXML
    private TableColumn<CustomerModel, Integer> idColumn;
    @FXML
    private TableColumn<CustomerModel, String> nameColumn;
    @FXML
    private TableColumn<CustomerModel, String> emailColumn;
    @FXML
    private TableColumn<CustomerModel, String> positionColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField positionField;

    private DaoModel dao;
//    private Main main;

    public void initialize() {
        dao = new DaoModel();

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());

        loadCustomerData();
    }

    private void loadCustomerData() {
        ObservableList<CustomerModel> customer = FXCollections.observableArrayList(dao.getAllCustomer());
        customerTable.setItems(customer);
    }
    
    
    @FXML
    private void handleAddCustomer() {
        String name = nameField.getText();
        String email = emailField.getText();
        String position = positionField.getText();

        // Generate a default username and password for the new employee
        String username = email.split("@")[0].toLowerCase();
        String password = "defaultPassword"; 

        // Create a user for the new employee and get the user ID
       // String hashedPassword = BcryptHash.hashPassword(password);
        int userId = dao.createUser(username, password, "customer");

        if (userId != -1) {
            CustomerModel customer = new CustomerModel(0, name, email, position, userId); // Use userId for the user_id

            if (dao.addCustomer(customer)) {
                loadCustomerData();
                nameField.clear();
                emailField.clear();
                positionField.clear();
            } else {
                showError("Failed to add customer.");
            }
        } else {
            showError("Failed to create user for the customer.");
        }
    }



    @FXML
    private void handleUpdateCustomer() {
        CustomerModel selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            String name = nameField.getText();
            String email = emailField.getText();
            String position = positionField.getText();

            selectedCustomer.setName(name);
            selectedCustomer.setEmail(email);
            selectedCustomer.setPosition(position);

            if (dao.updateCustomer(selectedCustomer)) {
                loadCustomerData();
                nameField.clear();
                emailField.clear();
                positionField.clear();
            } else {
                showError("Failed to update customer.");
            }
        } else {
            showInfo("Select an customer first.");
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        CustomerModel selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            if (dao.deleteCustomer(selectedCustomer.getName())) {
                loadCustomerData();
            } else {
                showError("Failed to delete customer.");
            }
        } else {
            showInfo("Select an customer first.");
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
    
//    public void setMain(Main main) {
//        this.main = main;
//    }
}
