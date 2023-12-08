package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
//import javafx.scene.layout.*;
import models.*;
import application.Main;
import utils.*;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Dao dao;
    private Main main;

    public void initialize() {
        dao = new Dao();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = dao.getUserByUsername(username);

        if (user != null && BcryptHash.verifyPassword(password, user.getPassword())) {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                dao.loadView("/views/AdminDashboardView.fxml", "Admin Dashboard");
            } else if ("customer".equalsIgnoreCase(user.getRole())) {
                // Fetch the Customer object using employee ID
                Customer customer = dao.getCustomerById(user.getEmployeeId());
                if (customer != null) {
                    main.showCustomerDashboard(customer);
                } else {
                    showError("Error retrieving customer information");
                }
            } else {
                showError("Invalid role");
            }
        } else {
            showError("Invalid username or password");
        }
    }


    @FXML
    private void handleSignup() {
        main.showSignupDialog();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}

